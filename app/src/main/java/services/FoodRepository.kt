package services

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import au.edu.swin.sdmd.myapp.datamodels.MenuItem
import com.datastax.oss.driver.api.core.CqlSession
import java.nio.file.Paths


class FoodRepository(context: Context) {

    private val dbHelper = FoodDbHelper(context)

    // add food to database using insert
    fun addFood(item: MenuItem) {
        try {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(FoodContract.FoodEntry.COL_ITEM_ID, item.itemID)
                put(FoodContract.FoodEntry.COL_ITEM_NAME, item.itemName)
                put(FoodContract.FoodEntry.COL_ITEM_PRICE, item.itemPrice)
                put(FoodContract.FoodEntry.COL_ITEM_DESC, item.itemShortDesc)
                put(FoodContract.FoodEntry.COL_ITEM_STAR, item.itemStars)
                put(FoodContract.FoodEntry.COL_ITEM_CATEGORY, item.itemTag)
                put(FoodContract.FoodEntry.COL_ITEM_IMAGE_URL, item.imageUrl)
            }
            db.insert(FoodContract.FoodEntry.TABLE_NAME, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // read all food items in the menu table
    fun getAllFood(): List<MenuItem> {
        CqlSession.builder()
            .withCloudSecureConnectBundle(Paths.get("D:/IT/Akathon/New folder/secure-connect-swinmealapp.zip"))
            .withAuthCredentials("AcRCBgaqoLkRzpruuQKQWpdS", "lXLR2SszZgnO4JiF,,ugKfELqmXwP74jR4_-Qq-T,f9P4OKRnoWeB0zOos,T0qg0+85KcW3MUdmZY1Zj3sC4x3dSTZZM8vXz0Ki08J_R4MHfQS9rGHR8twLI7NWfql2Q")
            .build().use { session ->
                // Select the release_version from the system.local table:
                val rs = session.execute("select name from menu.menu where category = 'drinks'")
                val row = rs.one()
                val count = row?.getString("name")
                println(count)
            }
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
        val menuList = mutableListOf<MenuItem>()

        return menuList
    }


    // delete a food item
    fun deleteFood(item: MenuItem) {
        try {
            val db = dbHelper.writableDatabase
            db.delete(
                FoodContract.FoodEntry.TABLE_NAME,
                "${FoodContract.FoodEntry.COL_ITEM_ID} = ?",
                arrayOf(item.itemID.toString())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // clear the database
    fun clearDatabase() {
        val db = dbHelper.writableDatabase
        db.delete(FoodContract.FoodEntry.TABLE_NAME, null, null)
    }
}
