package au.edu.swin.sdmd.swinmealapp.view

import au.edu.swin.sdmd.swinmealapp.adapters.RecyclerFoodItemAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.swin.sdmd.swinmealapp.R
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.navigation.NavigationView
import au.edu.swin.sdmd.swinmealapp.datamodels.MenuItem
import au.edu.swin.sdmd.swinmealapp.datamodels.order.CartItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import au.edu.swin.sdmd.swinmealapp.frontend.order.control.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import au.edu.swin.sdmd.swinmealapp.services.CustomerServices
import au.edu.swin.sdmd.swinmealapp.services.MenuItemServices

class MainActivity : AppCompatActivity(), RecyclerFoodItemAdapter.OnItemClickListener {

//    private lateinit var foodRepository: FoodRepository
    private val menuItemServices = MenuItemServices()
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var adapter: RecyclerFoodItemAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var cartRepository: CartRepository

    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var showAllSwitch: SwitchCompat

    private lateinit var topHeaderLL: LinearLayout
    private lateinit var topSearchLL: LinearLayout
    private lateinit var searchBox: SearchView
    private lateinit var foodCategoryCV: CardView
    private lateinit var showAllLL: LinearLayout
    private lateinit var slideshowCV: CardView
    private var searchIsActive = false

    var foodList = listOf<MenuItem>()
    val imageList = ArrayList<SlideModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the food repository
//        foodRepository = FoodRepository(this)
        // Initialize the cart repository
        cartRepository = CartRepository(this)
        // Clear cart table
        cartRepository.clearCartTable()
        // Clear food table
//        foodRepository.clearDatabase()

        loadNavigationDrawer()
        loadMenu()

        imageList.add(SlideModel("https://c0.wallpaperflare.com/preview/5/307/817/pizza-courier-online-cheese.jpg"))
        imageList.add(SlideModel("https://tasteforlife.com/sites/default/files/styles/facebook/public/healthy-recipes/soups/pho-bo-spicy-beef-vietnamese-noodle-soup/pho-bo-spicy-beef-vietnamese-noodle-soup.jpg?itok=3f4AcJoW", "Special Meal"))
        imageList.add(SlideModel("https://www.irishexaminer.com/cms_media/module_img/4648/2324177_111_seoimage2x1_young-woman-preparing-takeaway-healthy-food-inside-restaurant-during-picture-id1223841327.jpg"))

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)
    }

    // Loading the menu
    private fun loadMenu() {
        // Add data
//        foodRepository.addFood(
//            MenuItem(
//            0,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2F1.jpg?alt=media&token=b250a0fc-195c-4d45-b8eb-00fb48255e4c",
//            "Beef Burger",
//            4.0F,
//            "Delicious taste, convenience, and versatility",
//            "Burger",
//            3.0F)
//        )
//
//        foodRepository.addFood(
//            MenuItem(
//            1,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2Fpop_3.png?alt=media&token=5c125f26-6646-4745-b1d7-2482323c7e73",
//            "Seafood Pizza",
//            4.5F,
//            "Delicious taste and wide range of flavors and textures",
//            "Pizza",
//            4.0F)
//        )
//
//        foodRepository.addFood(
//            MenuItem(
//            2,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2Fcoca.jpg?alt=media&token=0fad8851-7585-4d13-8098-e50f7721dd46",
//            "Coca",
//            3.0F,
//            "Best with cold",
//            "Cold drinks",
//            3.5F)
//        )
//
//        foodRepository.addFood(
//            MenuItem(
//            3,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2Fchocolatecake.jpg?alt=media&token=d5e8758d-21d4-467b-a35d-8c2b0dc979dd",
//            "Chocolate Cake",
//            5.2F,
//            "Indulgent flavor, moist and tender texture",
//            "Cake",
//            5.0F)
//        )
//
//        foodRepository.addFood(
//            MenuItem(
//            4,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2Fapple.jpg?alt=media&token=e6bd784c-4cba-41f6-a115-d7e9081dfbff",
//            "Apple",
//            3.5F,
//            "Fiber, vitamins, and antioxidants",
//            "Fruits",
//            4.0F)
//        )
//
//        foodRepository.addFood(
//            MenuItem(
//            5,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2Fbutternutsoup.jpg?alt=media&token=7e51f242-f898-464f-8f19-fbf4fdf33a71",
//            "Butternut Soup",
//            4.8F,
//            "A smooth and velvety soup",
//            "Soup",
//            4.5F)
//        )
//
//        foodRepository.addFood(
//            MenuItem(
//            6,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2Fpho.jpg?alt=media&token=e7ebcd17-bbc3-42fc-aff5-480e22bc3823",
//            "Pho bo",
//            4.0F,
//            "Beef broth, rice noodles, and thinly sliced beef",
//            "Noodles",
//            5.0F)
//        )
//
//        foodRepository.addFood(
//            MenuItem(
//            7,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2Fsnack.jfif?alt=media&token=37ed71f1-499f-4b4a-9a02-bc636fd4582d",
//            "Lays Snack",
//            2.5F,
//            "Potato chips",
//            "Snacks",
//            3.0F)
//        )
//
//        foodRepository.addFood(
//            MenuItem(
//            8,
//            "https://firebasestorage.googleapis.com/v0/b/cafeteria-project-4ae2f.appspot.com/o/foodMenuItems%2Ffishsalad.jpg?alt=media&token=cbdfacb7-763b-424b-8189-52cc0044f38a",
//            "Fish Salad",
//            3.8F,
//            "Refreshing and healthy",
//            "Salad",
//            4.0F)
//        )

        // Display the list of food items
        //search function

        loadSearchTask(foodList)
        lifecycleScope.launch {
            foodList = withContext(Dispatchers.IO) { menuItemServices.getMenuItems() }
            adapter = RecyclerFoodItemAdapter(foodList as MutableList<MenuItem>, this@MainActivity)
            itemRecyclerView.adapter = adapter
        }

        itemRecyclerView = findViewById(R.id.items_recycler_view)

        linearLayoutManager = LinearLayoutManager(this)
        itemRecyclerView.layoutManager = linearLayoutManager



        GlobalScope.launch {
            //show all switching function
            showAllSwitch = findViewById(R.id.show_all_items_switch)
            showAllSwitch.setOnClickListener {
                if (showAllSwitch.isChecked) {
                    adapter.filterList(foodList as MutableList<MenuItem>) //display complete list
                    val container: LinearLayout = findViewById(R.id.food_categories_container)
                    for (ll in container.children) {
                        ll.alpha =
                            1.0f //change alpha value of all the category items, so it will indicate that they are not pressed
                    }
                }
            }
        }
    }

    //plus button handler
    override fun onPlusBtnClick(item: MenuItem) {
        item.quantity += 1
        val cartItem = CartItem(
            itemID = item.itemID,
            itemName = item.itemName,
            imageUrl = item.imageUrl,
            itemPrice = item.itemPrice,
            quantity = item.quantity,
            itemStars = item.itemStars,
            itemShortDesc = item.itemShortDesc,
//                foodID = item.itemID
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

        Log.i("quantity: ", item.quantity.toString())
        Log.i("cart: ", cartItem.toString())

    }

    //minus button handler
    override fun onMinusBtnClick(item: MenuItem) {
        GlobalScope.launch {
            if (item.quantity > 0) {
                item.quantity -= 1
                val cartItem = CartItem(
                    itemID = item.itemID,
                    itemName = item.itemName,
                    imageUrl = item.imageUrl,
                    itemPrice = item.itemPrice,
                    quantity = item.quantity,
                    itemStars = item.itemStars,
                    itemShortDesc = item.itemShortDesc,
//                    foodID = item.itemID
                )

                if (item.quantity == 0) {
                    // If quantity becomes 0, remove the item from cart
                    cartRepository.removeFromCart(cartItem)

                } else {
                    // Update the cart item quantity
                    cartRepository.decreaseCartItemQuantity(cartItem.itemID, cartItem.itemName, cartItem.itemPrice, cartItem.itemShortDesc, cartItem.imageUrl, cartItem.itemStars, cartItem.quantity)
                }
                Log.i("quantity: ", item.quantity.toString())
                Log.i("cart: ", cartItem.toString())
            }
        }
    }

    //displays the items which are of same category
    fun showTagItems(view: View) {
        val container: LinearLayout = findViewById(R.id.food_categories_container)
        for (ll in container.children) {
            ll.alpha = 0.5f
        }
        (view as LinearLayout).alpha = 1.0f
        val tag = ((view as LinearLayout).getChildAt(1) as TextView).text.toString().lowercase()
        val filterList = ArrayList<MenuItem>()
        for (item in foodList) {
            if (item.itemTag == tag) filterList.add(item)
        }
        adapter.filterList(filterList)
        showAllSwitch.isChecked = false
    }

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

    //search icon handler
    private fun loadSearchTask(foodList: List<MenuItem>) {
        topHeaderLL = findViewById(R.id.top_header)
        topSearchLL = findViewById(R.id.main_activity_top_search)
        searchBox = findViewById(R.id.search_menu_items)
        foodCategoryCV = findViewById(R.id.food_categories)
        showAllLL = findViewById(R.id.show_all)
        slideshowCV = findViewById(R.id.slideshowCV)

        findViewById<ImageView>(R.id.top_search).setOnClickListener {
            //hiding all the views which are above the items
            slideshowCV.visibility = ViewGroup.GONE
            topHeaderLL.visibility = ViewGroup.GONE
            foodCategoryCV.visibility = ViewGroup.GONE
            showAllLL.visibility = ViewGroup.GONE
            topSearchLL.visibility = ViewGroup.VISIBLE
            adapter.filterList(foodList as MutableList<MenuItem>)
            searchIsActive = true
        }

        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return false
            }
        })
    }

    //drawer navigation handler
    private fun loadNavigationDrawer() {
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val drawerDelay: Long = 150 //delay of the drawer to close
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_food_menu -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_profile -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                UserProfileActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
                R.id.nav_my_orders -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                CurrentOrderActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
                R.id.nav_orders_history -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                OrdersHistoryActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
                R.id.nav_share_app -> {
//                    shareApp()
                }
                R.id.nav_report_bug -> {
                    Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_contact_us -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }

                R.id.nav_settings -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
                R.id.nav_log_out -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                LoginUserActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
            }
            true
        }

        findViewById<ImageView>(R.id.nav_drawer).setOnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    //onback press
    override fun onBackPressed() {
        if (searchIsActive) {
            //un-hiding all the views which are above the items
            adapter.filter.filter("")
            topHeaderLL.visibility = ViewGroup.VISIBLE
            foodCategoryCV.visibility = ViewGroup.VISIBLE
            showAllLL.visibility = ViewGroup.VISIBLE
            topSearchLL.visibility = ViewGroup.GONE
            slideshowCV.visibility = ViewGroup.VISIBLE

            searchIsActive = false
            return
        }
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
//        if (doubleBackToExit) {
//            super.onBackPressed()
//            return
//        }
//        doubleBackToExit = true
//        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
//        Handler().postDelayed({ doubleBackToExit = false }, 2000)
    }
}