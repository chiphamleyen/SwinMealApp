package au.edu.swin.sdmd.swinmealapp.services

import au.edu.swin.sdmd.swinmealapp.datamodels.RequestWrapper
import au.edu.swin.sdmd.swinmealapp.datamodels.CurrentOrderItem
import au.edu.swin.sdmd.swinmealapp.datamodels.OrderHistoryItem
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class OrderServices {
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun addNewOrder(
        email: String,
        name: String,
        orderHistoryItem: OrderHistoryItem,
        currentOrderItem: CurrentOrderItem
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    val requestBody = RequestWrapper(orderHistoryItem, currentOrderItem)
                    val jsonMediaType = "application/json; charset=utf-8".toMediaType()
                    val requestBodyJson = jacksonObjectMapper().writeValueAsString(requestBody)
                    val requestBodyBody = requestBodyJson.toRequestBody(jsonMediaType)

                    val request = Request.Builder()
                        .url("http://10.0.2.2:8080/order/new-order?email=$email&name=$name")
                        .put(requestBodyBody)
                        .build()

                    val response = httpClient.newCall(request).execute()
                    if (!response.isSuccessful) {
                        throw RuntimeException("Failed to add new order")
                    }
                }
            } catch (e: Exception) {
                // Registration failed, handle the error
            }
        }
    }

    fun getAllCurrentOrders(email: String, callback: (List<CurrentOrderItem>?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val order = withContext(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url("http://10.0.2.2:8080/order/current-order?email=$email")
                        .build()

                    val response = httpClient.newCall(request).execute()
                    println(response)
                    if (!response.isSuccessful) {
                        throw RuntimeException("Failed to get all current orders")
                    }

                    val responseBody = response.body?.string()

                    jacksonObjectMapper().readValue(
                        responseBody ?: "",
                        object : TypeReference<List<CurrentOrderItem>>() {})
                }
                callback(order)
            } catch (e: Exception) {
                // Handle the error here

            }
        }
    }

    fun getAllHistoryOrders(email: String, callback: (List<OrderHistoryItem>?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val order = withContext(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url("http://10.0.2.2:8080/order/order-history?email=$email")
                        .build()

                    val response = httpClient.newCall(request).execute()
                    println(response)
                    if (!response.isSuccessful) {
                        throw RuntimeException("Failed to get menu items")
                    }

                    val responseBody = response.body?.string()

                    jacksonObjectMapper().readValue(
                        responseBody ?: "",
                        object : TypeReference<List<OrderHistoryItem>>() {})
                }
                callback(order)
            } catch (e: Exception) {
                // Handle the error here

            }
        }
    }

    fun confirmOrderDone(email: String, orderId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {

                    val request = Request.Builder()
                        .url("http://10.0.2.2:8080/order/order-received?email=$email&orderId=$orderId")
                        .put(RequestBody.create(null, ""))
                        .build()

                    httpClient.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            // Handle failure
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                // Update successful
                                println("Update current order successfully")
                            } else {
                                // Update failed
                                println("Failed to update current order")
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                // Registration failed, handle the error
            }
        }
    }

    fun orderCancel(email: String, orderId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {

                    val request = Request.Builder()
                        .url("http://10.0.2.2:8080/order/order-cancel?email=$email&orderId=$orderId")
                        .delete()
                        .build()

                    httpClient.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            // Handle failure
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                // Update successful
                                println("Delete current order successfully")
                            } else {
                                // Update failed
                                println("Failed to delete current order")
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                // Registration failed, handle the error
            }
        }
    }

    fun deleteHistoryData(email: String, orderId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("http://10.0.2.2:8080/order/history-delete?email=$email&orderId=$orderId")
                    .delete()
                    .build()

                val response = httpClient.newCall(request).execute()

                if (response.isSuccessful) {
                    // Handle successful response
                    println("History data deleted successfully")
                } else {
                    // Handle unsuccessful response
                    println("Failed to delete history data")
                }
            } catch (e: Exception) {
                // Handle the error here
                e.printStackTrace()
            }
        }
    }
}