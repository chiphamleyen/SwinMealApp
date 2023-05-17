package au.edu.swin.sdmd.swinmealapp.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.services.CustomerServices
import kotlin.math.round

class UserProfileActivity : AppCompatActivity() {

    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var actLevelTextView: TextView
    private lateinit var update: ImageView
    private lateinit var bmiTextView: TextView
    private lateinit var tdeeTextView: TextView

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
        bmiTextView = findViewById(R.id.profile_bmi)
        tdeeTextView = findViewById(R.id.profile_tdee)

        // Retrieve user email from Intent
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("email", "") ?: ""
        Log.i("intent", userEmail)

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
                heightTextView.text = it.height.toString() + " cm"
                weightTextView.text = it.weight.toString() + " kg"
                actLevelTextView.text = it.activityLevel
                val bmi = calculateBMI(it.weight, it.height)
                bmiTextView.text = "%.1f".format(bmi) + bmiRate(bmi)
                val tdee = calculateTDEE(it.weight, it.height, it.age, it.gender, it.activityLevel).toString()
                tdeeTextView.text = "~${tdee} calories per day"
                val editor = sharedPrefs.edit()
                editor.putString("bmi", bmiTextView.text.toString())
                editor.putString("tdee", tdee)
                editor.putString("activeLevel", it.activityLevel)
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

//    fun updateProfile(view: View) {
//        startActivity(
//            Intent(
//                this,
//                UserProfileSettings::class.java
//            )
//        )
//    }

    fun bmiRate (bmi : Float) : String {
        if (bmi < 18.5 ) return  " Underweight"
        else if (bmi in 18.5..25.0) return " Healthy"
        else if (bmi in 25.0..30.0) return " Overweight"
        else return  " Obesity"
    }

    fun calculateBMI(weight: Float?, height: Float?) : Float  {
        return weight.toString().toFloat()/(height.toString().toFloat()*height.toString().toFloat())*10000
    }

    fun calculateTDEE(weight: Float?, height: Float?, age: Int?, gender: String?, activityLevel: String?): Double {

        var bmr: Double = (9.99 * weight!!) + (6.25 * height!!) - (4.92 * age!!)

        if (gender == "male") {
            bmr += 5
        } else {
            bmr -= 161
        }

        val activityLevelCoefficient = when (activityLevel) {
            "sedentary" -> 1.2
            "lightly active" -> 1.375
            "moderately active" -> 1.55
            "very active" -> 1.725
            "extra active" -> 1.9
            else -> 1.0 // Default value if activityLevel is not recognized
        }

        return round(bmr * activityLevelCoefficient)
    }

}