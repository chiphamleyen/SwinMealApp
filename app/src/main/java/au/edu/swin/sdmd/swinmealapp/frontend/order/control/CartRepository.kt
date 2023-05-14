package au.edu.swin.sdmd.swinmealapp.frontend.order.control

import android.content.ContentValues
import android.content.Context
import au.edu.swin.sdmd.swinmealapp.datamodels.order.CartItem
import au.edu.swin.sdmd.swinmealapp.services.CartContract
import au.edu.swin.sdmd.swinmealapp.services.FoodContract
import au.edu.swin.sdmd.swinmealapp.services.FoodDbHelper

class CartRepository(context: Context) {
    private val dbHelper = FoodDbHelper(context)

    // update the quantity (increase 1) and insert the item
    fun increaseCartItemQuantity(cartId: String, name: String, price: Float, shortdesc: String, image: String, star: Float, quantity: Int) {
        val db = dbHelper.writableDatabase
        val query = "SELECT * FROM ${CartContract.CartEntry.CART_TABLE_NAME} WHERE ${CartContract.CartEntry.CART_ITEM_ID}='${cartId}';"
        val cursor = db.rawQuery(query, null);
        if (cursor.count > 0) {
//            val cv = ContentValues()
//            cv.put(CartContract.CartEntry.CART_ITEM_QTY, quantity)
            // Update qty
            val updateQuery = "UPDATE ${CartContract.CartEntry.CART_TABLE_NAME} SET ${CartContract.CartEntry.CART_ITEM_QTY} = ${CartContract.CartEntry.CART_ITEM_QTY} + 1 WHERE ${CartContract.CartEntry.CART_ITEM_ID} = ${cartId}"
            db.execSQL(updateQuery)
            return
        }

        val values = ContentValues().apply {
            put(CartContract.CartEntry.CART_ITEM_ID, cartId)
            put(CartContract.CartEntry.CART_ITEM_NAME, name)
            put(CartContract.CartEntry.CART_ITEM_PRICE, price.toDouble())
            put(CartContract.CartEntry.CART_ITEM_SHORT_DESC, shortdesc)
            put(CartContract.CartEntry.CART_IMAGE_URL, image)
            put(CartContract.CartEntry.CART_ITEM_STARS, star.toDouble())
            put(CartContract.CartEntry.CART_ITEM_QTY, quantity)
//            put(CartContract.CartEntry.COL_ITEM_ID, foodID)
        }
        db.insert(CartContract.CartEntry.CART_TABLE_NAME, null, values)
        cursor.close();
    }

    // update the quantity (decrease 1) and insert the item
    fun decreaseCartItemQuantity(cartId: String, name: String, price: Float, shortdesc: String, image: String, star: Float, quantity: Int) {
        val db = dbHelper.writableDatabase
        val query = "SELECT * FROM ${CartContract.CartEntry.CART_TABLE_NAME} WHERE ${CartContract.CartEntry.CART_ITEM_ID}='${cartId}';"
        val cursor = db.rawQuery(query, null);
        if (cursor.count > 0) {
//            val cv = ContentValues()
//            cv.put(CartContract.CartEntry.CART_ITEM_QTY, quantity)
            // Update qty
            val updateQuery = "UPDATE ${CartContract.CartEntry.CART_TABLE_NAME} SET ${CartContract.CartEntry.CART_ITEM_QTY} = ${CartContract.CartEntry.CART_ITEM_QTY} - 1 WHERE ${CartContract.CartEntry.CART_ITEM_ID} = $cartId"
            db.execSQL(updateQuery)
            return
        }
        cursor.close();

        val values = ContentValues().apply {
            put(CartContract.CartEntry.CART_ITEM_ID, cartId)
            put(CartContract.CartEntry.CART_ITEM_NAME, name)
            put(CartContract.CartEntry.CART_ITEM_PRICE, price)
            put(CartContract.CartEntry.CART_ITEM_SHORT_DESC, shortdesc)
            put(CartContract.CartEntry.CART_IMAGE_URL, image)
            put(CartContract.CartEntry.CART_ITEM_STARS, star)
            put(CartContract.CartEntry.CART_ITEM_QTY, quantity)
//            put(CartContract.CartEntry.COL_ITEM_ID, foodID)
        }
        db.insert(CartContract.CartEntry.CART_TABLE_NAME, null, values)
    }

    //delete item in current_cart table
    fun removeFromCart(item: CartItem) {
        val db = dbHelper.writableDatabase
        db.delete(
            CartContract.CartEntry.CART_TABLE_NAME,
            "${CartContract.CartEntry.CART_ITEM_ID} = ?",
            arrayOf(item.itemID.toString())
        )
    }

    // read all items in current_cart table
    fun readCartData(): MutableList<CartItem> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            CartContract.CartEntry.CART_ITEM_ID,
            CartContract.CartEntry.CART_ITEM_NAME,
            CartContract.CartEntry.CART_ITEM_PRICE,
            CartContract.CartEntry.CART_ITEM_SHORT_DESC,
            CartContract.CartEntry.CART_IMAGE_URL,
            CartContract.CartEntry.CART_ITEM_STARS,
            CartContract.CartEntry.CART_ITEM_QTY,
//            CartContract.CartEntry.COL_ITEM_ID,
        )
        val cursor = db.query(
            CartContract.CartEntry.CART_TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val cartList = mutableListOf<CartItem>()
        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(CartContract.CartEntry.CART_ITEM_ID))
                val name = getString(getColumnIndexOrThrow(CartContract.CartEntry.CART_ITEM_NAME))
                val price = getFloat(getColumnIndexOrThrow(CartContract.CartEntry.CART_ITEM_PRICE))
                val desc = getString(getColumnIndexOrThrow(CartContract.CartEntry.CART_ITEM_SHORT_DESC))
                val star = getFloat(getColumnIndexOrThrow(CartContract.CartEntry.CART_ITEM_STARS))
                val image = getString(getColumnIndexOrThrow(CartContract.CartEntry.CART_IMAGE_URL))
                val quantity = getInt(getColumnIndexOrThrow(CartContract.CartEntry.CART_ITEM_QTY))
//                val foodId = getString(getColumnIndexOrThrow(FoodContract.FoodEntry.COL_ITEM_ID))
                cartList.add(CartItem(id, image, name, price, desc, star, quantity))
            }
        }
        cursor.close()
        return cartList
    }

    //clear cart table
    fun clearCartTable() {
        try {
            val db = dbHelper.writableDatabase
            db.delete(CartContract.CartEntry.CART_TABLE_NAME, null, null)
        } catch (e: Exception) {
//            Toast.makeText(context, "Unable to delete the records", Toast.LENGTH_SHORT).show()
        }
    }

//        val query = "SELECT * FROM $CartContract.CartEntry.CART_TABLE_NAME WHERE $CartContract.CartEntry.CART_ITEM_ID='${item.itemID}';"
//        val cursor = db.rawQuery(query, null);
//        if (cursor.count > 0) {
//            // Update qty, cart item is already added
//            val values = ContentValues().apply {
//                put(CartContract.CartEntry.CART_ITEM_QTY, item.quantity)
//            }
//            db.update(
//                CartContract.CartEntry.CART_TABLE_NAME,
//                values,
//                "${CartContract.CartEntry.CART_ITEM_ID} = ?",
//                arrayOf(item.itemID.toString())
//            )
//        }
//        cursor.close();

//        val values = ContentValues().apply {
//            put(CartContract.CartEntry.CART_ITEM_QTY, item.quantity)
//        }
//        db.update(
//            CartContract.CartEntry.CART_TABLE_NAME,
//            values,
//            "${CartContract.CartEntry.CART_ITEM_ID} = ?",
//            arrayOf(item.itemID.toString())
//        )




//    fun insertCartItem(item: CartItem) {
//        val db = dbHelper.writableDatabase
//
//        val query = "SELECT * FROM $CartContract.CartEntry.CART_TABLE_NAME WHERE $CartContract.CartEntry.CART_ITEM_ID='${item.itemID}';"
//        val cursor = db.rawQuery(query, null);
//        if (cursor.count > 0) {
//            // Update qty, cart item is already added
//            val cv = ContentValues()
//            cv.put(CartContract.CartEntry.CART_ITEM_QTY, item.quantity)
//            db.update(CartContract.CartEntry.CART_TABLE_NAME, cv, "${CartContract.CartEntry.CART_ITEM_ID} = ?", arrayOf(item.itemID.toString()))
//            return
//        }
//        cursor.close();
//
//        val cv = ContentValues()
//        cv.put(CartContract.CartEntry.CART_ITEM_ID, item.itemID)
//        cv.put(CartContract.CartEntry.CART_ITEM_NAME, item.itemName)
//        cv.put(CartContract.CartEntry.CART_ITEM_PRICE, item.itemPrice)
//        cv.put(CartContract.CartEntry.CART_ITEM_SHORT_DESC, item.itemShortDesc)
//        cv.put(CartContract.CartEntry.CART_IMAGE_URL, item.imageUrl)
//        cv.put(CartContract.CartEntry.CART_ITEM_STARS, item.itemStars)
//        cv.put(CartContract.CartEntry.CART_ITEM_QTY, item.quantity)
//
//        val result = db.insert(CartContract.CartEntry.CART_TABLE_NAME, null, cv)
//        if (result == (-1).toLong()) {
////            Toast.makeText(context, "Failed to Insert Data", Toast.LENGTH_SHORT).show()
//        }
//    }
}