package au.edu.swin.sdmd.swinmealapp.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.services.UserDbHelper

class UserProfileActivity : AppCompatActivity() {

    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var employeeIDTextView: TextView
    private lateinit var mobileNumberTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        fullNameTextView = findViewById(R.id.profile_name)
        emailTextView = findViewById(R.id.profile_email)
        employeeIDTextView = findViewById(R.id.profile_employeeid)
        mobileNumberTextView = findViewById(R.id.profile_mobile)

        // Retrieve user email from Intent
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("email", null)
        Log.i("intent", userEmail.toString())

        val databaseHelper = UserDbHelper(this)
        val user = databaseHelper.getUserProfile(userEmail.toString())
        Log.i("profile", user.toString())
            if (user != null) {  // Check if user email is not null
                // Populate user profile data to TextViews
                fullNameTextView.text = user.name
                emailTextView.text = user.email
                employeeIDTextView.text = user.employeeId
                mobileNumberTextView.text = user.mobile
            } else {
                // Failed to retrieve user profile
                Toast.makeText(this, "Failed to retrieve user profile", Toast.LENGTH_SHORT).show()
            }
    }

    fun goBack(view: View) {onBackPressed()}

    fun updateProfile(view: View) {}

}