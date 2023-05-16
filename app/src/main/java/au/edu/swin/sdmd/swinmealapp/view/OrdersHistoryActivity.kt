package au.edu.swin.sdmd.swinmealapp.view

import au.edu.swin.sdmd.swinmealapp.adapters.RecyclerOrderHistoryAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.datamodels.OrderHistoryItem
import au.edu.swin.sdmd.swinmealapp.services.OrderServices

class OrdersHistoryActivity : AppCompatActivity() {
    private var orderHistoryList = ArrayList<OrderHistoryItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerOrderHistoryAdapter

    private lateinit var deleteRecordsIV : ImageView

    private var orderServices = OrderServices()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_history)

        recyclerView = findViewById(R.id.order_history_recycler_view)
        recyclerAdapter = RecyclerOrderHistoryAdapter(this, orderHistoryList)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        deleteRecordsIV = findViewById(R.id.order_history_delete_records_iv)

        loadOrderHistoryListFromDatabase()
    }

    //read data from database
    private fun loadOrderHistoryListFromDatabase() {
        val sharedPrefs = getSharedPreferences("Order", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("emailOrder", "") ?: ""

        orderServices.getAllHistoryOrders(userEmail) { data ->
            data?.let {
                if (data.size == 0) {
                    deleteRecordsIV.visibility = ViewGroup.INVISIBLE
                }

                //display data
                findViewById<LinearLayout>(R.id.order_history_empty_indicator_ll).visibility =
                    ViewGroup.GONE
                for (i in 0 until data.size) {
                    val item = OrderHistoryItem()
                    item.date = data[i].date
                    item.orderId = data[i].orderId
                    item.orderStatus = data[i].orderStatus
                    item.orderPayment = data[i].orderPayment
                    item.price = data[i].price

                    if (item.orderStatus == "Order Done" || item.orderStatus == "Order Cancel"){
                        orderHistoryList.add(item)
                        deleteRecordsIV.setOnClickListener { deleteOrderHistoryRecords(orderHistoryList) }
                        orderHistoryList.reverse()
                    }

                    recyclerAdapter.notifyItemRangeInserted(0, data.size)
                    Log.i("history act", item.toString())
                }
            }
        }
    }

    //delete all order history
    private fun deleteOrderHistoryRecords(orderHistoryList: ArrayList<OrderHistoryItem>) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want delete all your order history?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, _ ->
                val sharedPrefs = getSharedPreferences("Order", Context.MODE_PRIVATE)
                val userEmail = sharedPrefs.getString("emailOrder", "") ?: ""

                for (i in orderHistoryList){
                    var orderId = i.orderId
                    Log.i("orderid history", orderId)
                    orderServices.deleteHistoryData(userEmail, orderId)
                }

                deleteRecordsIV.visibility = ViewGroup.INVISIBLE
                findViewById<LinearLayout>(R.id.order_history_empty_indicator_ll).visibility = ViewGroup.VISIBLE

                val size = orderHistoryList.size
                orderHistoryList.clear()
                recyclerAdapter.notifyItemRangeRemoved(0, size)

                dialogInterface.dismiss()
            } )
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
            })
            .create().show()
    }

    fun goBack(view: View) {onBackPressed()}
}