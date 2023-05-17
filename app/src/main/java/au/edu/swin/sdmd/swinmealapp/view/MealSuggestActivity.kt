package au.edu.swin.sdmd.swinmealapp.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.adapters.RecyclerFoodItemAdapter
import au.edu.swin.sdmd.swinmealapp.datamodels.MenuItem
import au.edu.swin.sdmd.swinmealapp.services.MenuItemServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round
import kotlin.properties.Delegates

class MealSuggestActivity : AppCompatActivity(), RecyclerFoodItemAdapter.OnItemClickListener {
    private val menuItemServices = MenuItemServices()
    private lateinit var adapter: RecyclerFoodItemAdapter
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var bmiTextView: TextView
    private lateinit var tdeeTextView: TextView
    private lateinit var actLevelTextView: TextView
    private lateinit var recMealCalTextView: TextView

    private var foodList = listOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_suggest)

        bmiTextView = findViewById(R.id.profile_bmi)
        tdeeTextView = findViewById(R.id.profile_tdee)
        actLevelTextView = findViewById(R.id.profile_activityLevel)
        recMealCalTextView = findViewById(R.id.rec_meal_cal)

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        bmiTextView.text = sharedPrefs.getString("bmi", "") ?: ""
        tdeeTextView.text = sharedPrefs.getString("tdee", "") ?: ""
        actLevelTextView.text = sharedPrefs.getString("activeLevel", "") ?: ""
        val rec_meal_cal = round(tdeeTextView.text.toString().toDouble()/3)
        val rec_meal_cal_low = round(rec_meal_cal*95/100)
        val rec_meal_cal_high = round(rec_meal_cal*105/100)
        recMealCalTextView.text = "${rec_meal_cal_low} to ${rec_meal_cal_high}"

        loadSuggestMenu(rec_meal_cal_low, rec_meal_cal_high)
    }

    private fun loadSuggestMenu(rec_meal_cal_low: Double, rec_meal_cal_high: Double) {
        lifecycleScope.launch {
            foodList = withContext(Dispatchers.IO) { menuItemServices.getMenuItems() }
            adapter = RecyclerFoodItemAdapter(foodList as MutableList<MenuItem>, this@MealSuggestActivity)
            val suggestList = ArrayList<MenuItem>()
            for (item in foodList) {
                if (item.calories in rec_meal_cal_low..rec_meal_cal_high)
                    suggestList.add(item)
            }
            suggestList.shuffle()
            adapter.filterList(suggestList)
            itemRecyclerView.adapter = adapter
        }

        itemRecyclerView = findViewById(R.id.items_recycler_view)

        linearLayoutManager = LinearLayoutManager(this)
        itemRecyclerView.layoutManager = linearLayoutManager
    }

    fun goBack(view: View) {onBackPressed()}

    override fun onPlusBtnClick(item: MenuItem) {
        TODO("Not yet implemented")
    }

    override fun onMinusBtnClick(item: MenuItem) {
        TODO("Not yet implemented")
    }


}