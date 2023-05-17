package au.edu.swin.sdmd.swinmealapp.view

import akathon.cos30017.swin_meal_backend.datamodel.SuggestMeal
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.adapters.MealSuggestAdapter
import au.edu.swin.sdmd.swinmealapp.adapters.RecyclerFoodItemAdapter
import au.edu.swin.sdmd.swinmealapp.datamodels.CartItem
import au.edu.swin.sdmd.swinmealapp.datamodels.MenuItem
import au.edu.swin.sdmd.swinmealapp.services.CartRepository
import au.edu.swin.sdmd.swinmealapp.services.MenuItemServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round
import kotlin.properties.Delegates

class MealSuggestActivity : AppCompatActivity(), MealSuggestAdapter.OnItemClickListener {
    private val menuItemServices = MenuItemServices()
    private lateinit var adapter: MealSuggestAdapter
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var bmiTextView: TextView
    private lateinit var tdeeTextView: TextView
    private lateinit var actLevelTextView: TextView
    private lateinit var recMealCalTextView: TextView

    private lateinit var cartRepository: CartRepository

//    private var foodList = listOf<MenuItem>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_suggest)

        bmiTextView = findViewById(R.id.profile_bmi)
        tdeeTextView = findViewById(R.id.profile_tdee)
        actLevelTextView = findViewById(R.id.profile_activityLevel)
        recMealCalTextView = findViewById(R.id.rec_meal_cal)

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        bmiTextView.text = sharedPrefs.getString("bmi", "") ?: ""
        actLevelTextView.text = sharedPrefs.getString("activeLevel", "") ?: ""
        tdeeTextView.text = sharedPrefs.getString("tdee", "") ?: ""

        val rec_meal_cal = round(tdeeTextView.text.toString().toDouble()/3)
        val rec_meal_cal_low = round(rec_meal_cal*95/100)
        val rec_meal_cal_high = round(rec_meal_cal*105/100)
        recMealCalTextView.text = "${rec_meal_cal_low} to ${rec_meal_cal_high}"

        val regenerateBtn = findViewById<Button>(R.id.regenerate_suggested_meals)
        regenerateBtn.setOnClickListener() {
            loadSuggestMenu(rec_meal_cal_low.toFloat(), rec_meal_cal_high.toFloat())
        }
        loadSuggestMenu(rec_meal_cal_low.toFloat(), rec_meal_cal_high.toFloat())

        cartRepository = CartRepository(this)
    }

    private fun loadSuggestMenu(rec_meal_cal_low: Float, rec_meal_cal_high: Float) {
        lifecycleScope.launch {
//            foodList = withContext(Dispatchers.IO) { menuItemServices.getMenuItems() }
//            adapter = RecyclerFoodItemAdapter(foodList as MutableList<MenuItem>, this@MealSuggestActivity)
//            val suggestList = ArrayList<MenuItem>()
//            for (item in foodList) {
//                if (item.calories in rec_meal_cal_low..rec_meal_cal_high)
//                    suggestList.add(item)
//            }
//            suggestList.shuffle()
//            adapter.filterList(suggestList)
//            itemRecyclerView.adapter = adapter
            var suggestMeals = withContext(Dispatchers.IO) { menuItemServices.getMenuSuggest(rec_meal_cal_low, rec_meal_cal_high).shuffled() }
            suggestMeals = if(suggestMeals.size > 5) suggestMeals.subList(0,5) else suggestMeals
            adapter = MealSuggestAdapter(suggestMeals as MutableList<SuggestMeal>, this@MealSuggestActivity)
            itemRecyclerView.adapter = adapter
//            println(suggestMeals.size)
//            println(adapter.itemCount)
        }

        itemRecyclerView = findViewById(R.id.items_recycler_view)

        linearLayoutManager = LinearLayoutManager(this)
        itemRecyclerView.layoutManager = linearLayoutManager
    }

    fun goBack(view: View) {onBackPressed()}

    //show bottom fragment
    fun showBottomDialog(view: View) {
        val bottomDialog = BottomSheetSelectedItemDialog()
        val bundle = Bundle()

        var totalPrice = 0.0f
        var totalItems = 0

        for (item in cartRepository.readCartData()) {
            totalPrice += item.itemPrice * item.quantity
            totalItems += item.quantity
        }

        bundle.putFloat("totalPrice", totalPrice)
        bundle.putInt("totalItems", totalItems)
        // bundle.putParcelableArrayList("orderedList", recyclerFoodAdapter.getOrderedList() as ArrayList<out Parcelable?>?)

        bottomDialog.arguments = bundle
        bottomDialog.show(supportFragmentManager, "BottomSheetDialog")
    }

    //user icon handler
    fun showUserProfile(view: View) {
        startActivity(
            Intent(
                this,
                UserProfileActivity::class.java
            )
        )
    }

    override fun onFoodClick1(item: SuggestMeal) {
        val bottomDialog = NutritionsFragment()
        val bundle = Bundle()

        bundle.putString("food_name", item.food1.itemName)
        bundle.putString("food_calories", item.food1.calories.toString())
        bundle.putString("food_protein", item.food1.protein.toString())
        bundle.putString("food_carbohydrate", item.food1.carbohydrate.toString())
        bundle.putString("food_fat", item.food1.fat.toString())

        bottomDialog.arguments
    }

    override fun onFoodClick2(item: SuggestMeal) {
        val bottomDialog = NutritionsFragment()
        val bundle = Bundle()

        bundle.putString("food_name", item.food2.itemName)
        bundle.putString("food_calories", item.food2.calories.toString())
        bundle.putString("food_protein", item.food2.protein.toString())
        bundle.putString("food_carbohydrate", item.food2.carbohydrate.toString())
        bundle.putString("food_fat", item.food2.fat.toString())

        bottomDialog.arguments
    }

    override fun onPlusBtn1Click(item: SuggestMeal) {
        item.food1.quantity += 1
        val cartItem = CartItem(
            itemID = item.food1.itemID.toString(),
            itemName = item.food1.itemName,
            imageUrl = item.food1.imageUrl,
            itemPrice = item.food1.itemPrice,
            quantity = item.food1.quantity,
            itemStars = item.food1.itemStars,
            itemShortDesc = item.food1.itemShortDesc,
        )

        cartRepository.increaseCartItemQuantity(
            cartItem.itemID,
            cartItem.itemName,
            cartItem.itemPrice,
            cartItem.itemShortDesc,
            cartItem.imageUrl,
            cartItem.itemStars,
            cartItem.quantity,
//                item.itemID
        )

        Log.i("quantity food1: ", item.food1.quantity.toString())
        Log.i("cart food1: ", cartItem.toString())
    }

    override fun onPlusBtn2Click(item: SuggestMeal) {
        item.food2.quantity += 1
        val cartItem = CartItem(
            itemID = item.food2.itemID.toString(),
            itemName = item.food2.itemName,
            imageUrl = item.food2.imageUrl,
            itemPrice = item.food2.itemPrice,
            quantity = item.food2.quantity,
            itemStars = item.food2.itemStars,
            itemShortDesc = item.food2.itemShortDesc,
        )

        cartRepository.increaseCartItemQuantity(
            cartItem.itemID,
            cartItem.itemName,
            cartItem.itemPrice,
            cartItem.itemShortDesc,
            cartItem.imageUrl,
            cartItem.itemStars,
            cartItem.quantity,
//                item.itemID
        )

        Log.i("quantity food2: ", item.food2.quantity.toString())
        Log.i("cart food2: ", cartItem.toString())
    }

    override fun onMinusBtn1Click(item: SuggestMeal) {
        GlobalScope.launch {
            if (item.food1.quantity > 0) {
                item.food1.quantity -= 1
                val cartItem = CartItem(
                    itemID = item.food1.itemID.toString(),
                    itemName = item.food1.itemName,
                    imageUrl = item.food1.imageUrl,
                    itemPrice = item.food1.itemPrice,
                    quantity = item.food1.quantity,
                    itemStars = item.food1.itemStars,
                    itemShortDesc = item.food1.itemShortDesc,
                )

                if (item.food1.quantity == 0) {
                    // If quantity becomes 0, remove the item from cart
                    cartRepository.removeFromCart(cartItem)

                } else {
                    // Update the cart item quantity
                    cartRepository.decreaseCartItemQuantity(cartItem.itemID, cartItem.itemName, cartItem.itemPrice, cartItem.itemShortDesc, cartItem.imageUrl, cartItem.itemStars, cartItem.quantity)
                }
                Log.i("quantity food1: ", item.food1.quantity.toString())
                Log.i("cart food1: ", cartItem.toString())
            }
        }
    }

    override fun onMinusBtn2Click(item: SuggestMeal) {
        GlobalScope.launch {
            if (item.food2.quantity > 0) {
                item.food2.quantity -= 1
                val cartItem = CartItem(
                    itemID = item.food2.itemID.toString(),
                    itemName = item.food2.itemName,
                    imageUrl = item.food2.imageUrl,
                    itemPrice = item.food2.itemPrice,
                    quantity = item.food2.quantity,
                    itemStars = item.food2.itemStars,
                    itemShortDesc = item.food2.itemShortDesc,
                )

                if (item.food2.quantity == 0) {
                    // If quantity becomes 0, remove the item from cart
                    cartRepository.removeFromCart(cartItem)

                } else {
                    // Update the cart item quantity
                    cartRepository.decreaseCartItemQuantity(cartItem.itemID, cartItem.itemName, cartItem.itemPrice, cartItem.itemShortDesc, cartItem.imageUrl, cartItem.itemStars, cartItem.quantity)
                }
                Log.i("quantity food2: ", item.food2.quantity.toString())
                Log.i("cart food2: ", cartItem.toString())
            }
        }
    }
}