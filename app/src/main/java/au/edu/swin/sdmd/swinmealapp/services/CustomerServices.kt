package au.edu.swin.sdmd.swinmealapp.services

import android.util.Log
import au.edu.swin.sdmd.swinmealapp.datamodels.Customer
import au.edu.swin.sdmd.swinmealapp.datamodels.MenuItem
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class CustomerServices {
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun registerUser(customer: Customer, callback: (Response, String?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    val jsonMediaType = "application/json; charset=utf-8".toMediaType()
                    val requestBody = jacksonObjectMapper().writeValueAsString(customer)
                        .toRequestBody(jsonMediaType)

                    val request = Request.Builder()
                        .url("http://10.0.2.2:8080/customer/sign-up")
                        .put(requestBody)
                        .build()

//                    val response = httpClient.newCall(request).execute()
//                    println(response)
//                    if (!response.isSuccessful) {
//                        throw RuntimeException("Failed to register user")
//                    }

                    httpClient.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            val responseBody = response.body?.string()
                            callback(response, responseBody)
                        }

                        override fun onFailure(call: Call, e: IOException) {
//                            callback(response, e.message)
                        }
                    })
                }
                // Registration successful, handle the response or perform any other action
            } catch (e: Exception) {
                // Registration failed, handle the error
            }
        }
    }

     fun customerLogin(email: String, password: String, callback: (Response, String?) -> Unit) {
         GlobalScope.launch(Dispatchers.Main) {
             try {
                 withContext(Dispatchers.IO) {
                     val loginUrl =
                         "http://10.0.2.2:8080/customer/login" // Replace with your login URL

                     val httpClient = OkHttpClient()

                     val request = Request.Builder()
                         .url("$loginUrl?email=$email&password=$password")
                         .get()
                         .build()

//            val response = httpClient.newCall(request).execute()
//            println(response)
//            if (!response.isSuccessful) {
//                throw RuntimeException("Failed to login user")
//            }
//            return@withContext response
                     httpClient.newCall(request).enqueue(object : Callback {
                         override fun onResponse(call: Call, response: Response) {
                             val responseBody = response.body?.string()
                             callback(response, responseBody)
                         }

                         override fun onFailure(call: Call, e: IOException) {
//                            callback(response, e.message)
                         }
                     })
                 }
             } catch (e: Exception) {
                 // Registration failed, handle the error
             }
         }
    }

    fun getUserProfile(email: String, callback: (Customer?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val customer = withContext(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url("http://10.0.2.2:8080/customer/getProfile?email=$email")
                        .build()

                    val response = httpClient.newCall(request).execute()
                    println(response)
                    if (!response.isSuccessful) {
                        throw RuntimeException("Failed to retrieve user profile")
                    }

                    val responseBody = response.body?.string()
                    jacksonObjectMapper().readValue(
                        responseBody ?: "",
                        Customer::class.java
                    )
                }

                callback(customer)
            } catch (e: Exception) {
                // Handle the error here
                callback(null)
                Log.e("UserProfile", "Failed to retrieve user profile: ${e.message}")
            }
        }
    }

    fun updateProfile(email: String, gender: String, age: Int,
                      height: Float, weight: Float, activityLevel: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    val updateUrl =
                        "http://10.0.2.2:8080/customer/editHealthProfile" // Replace with your login URL

                    val httpClient = OkHttpClient()

                    val request = Request.Builder()
                        .url("$updateUrl?email=$email&gender=$gender&age=$age&height=$height&weight=$weight&activityLevel=$activityLevel")
                        .get()
                        .build()

//            val response = httpClient.newCall(request).execute()
//            println(response)
//            if (!response.isSuccessful) {
//                throw RuntimeException("Failed to login user")
//            }
//            return@withContext response
                    httpClient.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            // Handle failure
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                // Update successful
                                println("Customer health profile updated successfully")
                            } else {
                                // Update failed
                                println("Failed to update customer health profile")
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                // Registration failed, handle the error
            }
        }
    }
}