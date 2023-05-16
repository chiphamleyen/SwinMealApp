package au.edu.swin.sdmd.swinmealapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.services.CustomerServices
import com.google.android.material.textfield.TextInputEditText

class UserProfileSettings : AppCompatActivity() {
    //    private lateinit var fullNameTIL: TextInputEditText
    private lateinit var genderTIL: TextInputEditText
    private lateinit var ageTIL: TextInputEditText
    private lateinit var heightTIL: TextInputEditText
    private lateinit var weightTIL: TextInputEditText
    private lateinit var activeLevelList: Spinner
    private var selectedActiveLevel: String? = null
    private lateinit var saveBtn: Button

    private val customerServices = CustomerServices()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_settings)

        genderTIL = findViewById(R.id.gender)
        ageTIL = findViewById(R.id.age)
        heightTIL = findViewById(R.id.height)
        weightTIL = findViewById(R.id.weight)
        activeLevelList = findViewById(R.id.activeLevel)
        activeLevelList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedActiveLevel = parent?.getItemAtPosition(position) as? String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedActiveLevel = null
            }
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.activeLevel_array, // Replace with your own array resource name
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            activeLevelList.adapter = adapter
        }

        saveBtn = findViewById(R.id.saveBtn)

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("email", "") ?: ""
        Log.i("profile update email", userEmail.toString())

//        val userEmail = "chipham@gmail.com"

        saveBtn.setOnClickListener {
            val gender = genderTIL.text.toString()
            val age = ageTIL.text.toString()
            val height = ageTIL.text.toString()
            val weight = weightTIL.text.toString()
            val actLevel = selectedActiveLevel.toString().lowercase()
            if (gender.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty() || actLevel.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                Log.i("profile update", gender + age + height + weight + actLevel)
                updateProfile(userEmail, gender, age.toInt(), height.toFloat(), weight.toFloat(), actLevel)
            }
        }
    }

    private fun updateProfile (email: String, gender: String, age: Int,
                               height: Float, weight: Float, activityLevel: String) {

        customerServices.updateProfile(email, gender, age, height, weight, activityLevel)
        startActivity(Intent(this, UserProfileActivity::class.java))
        finish()
    }
}