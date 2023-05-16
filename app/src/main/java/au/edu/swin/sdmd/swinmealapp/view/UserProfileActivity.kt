package au.edu.swin.sdmd.swinmealapp.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.services.CustomerServices

class UserProfileActivity : AppCompatActivity() {

    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
//    private lateinit var employeeIDTextView: TextView
//    private lateinit var mobileNumberTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var actLevelTextView: TextView
    private lateinit var update: ImageView

    private val menuItemServices = CustomerServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        fullNameTextView = findViewById(R.id.profile_name)
        emailTextView = findViewById(R.id.profile_email)
        genderTextView = findViewById(R.id.profile_gender)
        ageTextView = findViewById(R.id.profile_age)
        heightTextView = findViewById(R.id.profile_height)
        weightTextView = findViewById(R.id.profile_weight)
        actLevelTextView = findViewById(R.id.profile_activityLevel)
        update = findViewById(R.id.update_profile)
//        employeeIDTextView = findViewById(R.id.profile_employeeid)
//        mobileNumberTextView = findViewById(R.id.profile_mobile)

        // Retrieve user email from Intent
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("email", "") ?: ""
        Log.i("intent", userEmail.toString())

//        val customer = menuItemServices.getUserProfile(userEmail)

//        val databaseHelper = UserDbHelper(this)
//        val user = databaseHelper.getUserProfile(userEmail.toString())
//        Log.i("profile", user.toString())

//            if (user != null) {  // Check if user email is not null
                // Populate user profile data to TextViews
        update.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UserProfileSettings::class.java
                )
            )
        }

        menuItemServices.getUserProfile(userEmail) { customer ->
            customer?.let {
                // Populate user profile data to TextViews
                fullNameTextView.text = it.name
                emailTextView.text = it.email
                genderTextView.text = it.gender
                ageTextView.text = it.age.toString()
                heightTextView.text = it.height.toString()
                weightTextView.text = it.weight.toString()
                actLevelTextView.text = it.activityLevel

                val sharedPrefs = getSharedPreferences("Order", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.putString("emailOrder", it.email)
                editor.putString("nameOrder", it.name)
                editor.apply()
            } ?: run {
                // Handle the case when customer is null (error occurred)
                Toast.makeText(this, "Failed to retrieve user profile", Toast.LENGTH_SHORT).show()
                Log.e("UserProfile", "Failed to retrieve user profile")
            }
        }

//                employeeIDTextView.text = user.employeeId
//                mobileNumberTextView.text = user.mobile
//            } else {
                // Failed to retrieve user profile
//                Toast.makeText(this, "Failed to retrieve user profile", Toast.LENGTH_SHORT).show()
//            }
    }

    fun goBack(view: View) {onBackPressed()}

}