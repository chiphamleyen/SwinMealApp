package services

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import au.edu.swin.sdmd.myapp.datamodels.MenuItem
import com.datastax.oss.driver.api.core.CqlSession
import java.io.FileInputStream
import java.net.URL
import java.nio.file.Paths


//class FoodRepository(context: Context) {
//
//    private val dbHelper = FoodDbHelper(context)
//
//    // add food to database using insert
//    fun addFood(item: MenuItem) {
//        try {
//            val db = dbHelper.writableDatabase
//            val values = ContentValues().apply {
//                put(FoodContract.FoodEntry.COL_ITEM_ID, item.itemID)
//                put(FoodContract.FoodEntry.COL_ITEM_NAME, item.itemName)
//                put(FoodContract.FoodEntry.COL_ITEM_PRICE, item.itemPrice)
//                put(FoodContract.FoodEntry.COL_ITEM_DESC, item.itemShortDesc)
//                put(FoodContract.FoodEntry.COL_ITEM_STAR, item.itemStars)
//                put(FoodContract.FoodEntry.COL_ITEM_CATEGORY, item.itemTag)
//                put(FoodContract.FoodEntry.COL_ITEM_IMAGE_URL, item.imageUrl)
//            }
//            db.insert(FoodContract.FoodEntry.TABLE_NAME, null, values)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    // read all food items in the menu table
//    fun getAllFood(): List<MenuItem> {
////        val menuList = mutableListOf<MenuItem>()
////        println(Paths.get("connection_bundles/secure-connect-workshops.zip"))
////        val session = CqlSession.builder()
////            .withCloudSecureConnectBundle(URL("https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/a48815fc-60d0-4f57-832d-5aa338f84b18-1/secure-connect-workshops.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76S2JCB77W%2F20230430%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20230430T035836Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=fff892709813cdf6f9e34513fbb3b38320491e18f06e0ac6"))
////            .withAuthCredentials("AcRCBgaqoLkRzpruuQKQWpdS", "lXLR2SszZgnO4JiF,,ugKfELqmXwP74jR4_-Qq-T,f9P4OKRnoWeB0zOos,T0qg0+85KcW3MUdmZY1Zj3sC4x3dSTZZM8vXz0Ki08J_R4MHfQS9rGHR8twLI7NWfql2Q")
////            .build()
////
////        val rs = session.execute("select * from sensor_data.menu where category = 'bbqs'")
////        for (row in rs) {
////            val category = row.getString("category")!!
////            val price = row.getString("price")!!.toFloat()
////            val description = row.getString("dsc")!!
////            val id = row.getString("id")!!
////            val img = row.getString("img")!!
////            val name = row.getString("name")!!
////            val rate = row.getString("rate")!!.toFloat()
////            val newMenuItem = MenuItem(id, img, name, price, description, category, rate)
////            menuList.add(newMenuItem)
////        }
//        val db = dbHelper.readableDatabase
//        val projection = arrayOf(
//            FoodContract.FoodEntry.COL_ITEM_ID,
//            FoodContract.FoodEntry.COL_ITEM_NAME,
//            FoodContract.FoodEntry.COL_ITEM_PRICE,
//            FoodContract.FoodEntry.COL_ITEM_DESC,
//            FoodContract.FoodEntry.COL_ITEM_STAR,
//            FoodContract.FoodEntry.COL_ITEM_CATEGORY,
//            FoodContract.FoodEntry.COL_ITEM_IMAGE_URL,
//        )
//        val cursor = db.query(
//            FoodContract.FoodEntry.TABLE_NAME,
//            projection,
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//        val menuList = mutableListOf<MenuItem>()
//        with(cursor) {
//            while (moveToNext()) {
//                val id = getLong(getColumnIndexOrThrow(FoodContract.FoodEntry.COL_ITEM_ID))
//                val name = getString(getColumnIndexOrThrow(FoodContract.FoodEntry.COL_ITEM_NAME))
//                val price = getFloat(getColumnIndexOrThrow(FoodContract.FoodEntry.COL_ITEM_PRICE))
//                val desc = getString(getColumnIndexOrThrow(FoodContract.FoodEntry.COL_ITEM_DESC))
//                val star = getFloat(getColumnIndexOrThrow(FoodContract.FoodEntry.COL_ITEM_STAR))
//                val category = getString(getColumnIndexOrThrow(FoodContract.FoodEntry.COL_ITEM_CATEGORY))
//                val image = getString(getColumnIndexOrThrow(FoodContract.FoodEntry.COL_ITEM_IMAGE_URL))
//                menuList.add(MenuItem(id, image, name, price, desc, category, star))
//            }
//        }
//        cursor.close()
//        return menuList
////        session.close()
////        Log.d("Menu", menuList.toString())
////        return menuList
//    }
//
//
//    // delete a food item
//    fun deleteFood(item: MenuItem) {
//        try {
//            val db = dbHelper.writableDatabase
//            db.delete(
//                FoodContract.FoodEntry.TABLE_NAME,
//                "${FoodContract.FoodEntry.COL_ITEM_ID} = ?",
//                arrayOf(item.itemID.toString())
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
////
////    // clear the database
//    fun clearDatabase() {
//        val db = dbHelper.writableDatabase
//        db.delete(FoodContract.FoodEntry.TABLE_NAME, null, null)
//    }
//}
