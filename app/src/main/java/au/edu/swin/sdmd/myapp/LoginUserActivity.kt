package au.edu.swin.sdmd.myapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking
import services.MenuItemServices
import services.UserDbHelper

class LoginUserActivity : AppCompatActivity() {

    private lateinit var emailTIL: TextInputEditText
    private lateinit var passwordTIL: TextInputEditText
    private lateinit var btnLogin: Button
    private val menuItemServices = MenuItemServices()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        emailTIL = findViewById(R.id.emailLogin)
        passwordTIL = findViewById(R.id.passLogin)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = emailTIL.text.toString().trim()
            val password = passwordTIL.text.toString().trim()

            userLogin(email, password)
        }
    }

    //validation and start intent. share preferences
    private fun userLogin(email: String, password: String) {
//        val databaseHelper = UserDbHelper(this)
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            runBlocking {
//            val isLoggedIn = databaseHelper.loginUser(email, password)
//            if (isLoggedIn) {
                // Login successful
            val response = menuItemServices.customerLogin(email, password)

            if (response.isSuccessful) {
                Toast.makeText(this@LoginUserActivity, "Login successful", Toast.LENGTH_SHORT).show()
//
//                val emailIntent = Intent(this, UserProfileActivity::class.java)
//                emailIntent.putExtra("email", email)
//                Log.i("intentPut", email)
//
                val loginIntent = Intent(this@LoginUserActivity, MainActivity::class.java)
                startActivity(loginIntent)
//
//                // Save email data to SharedPreferences
//                val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//                val editor = sharedPrefs.edit()
//                editor.putString("email", email)
//                editor.apply()

                // Finish the current activity
                finish()
                } else {
                    // Login failed
                    Toast.makeText(this@LoginUserActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun openRegisterActivity(view: View) {
        startActivity(Intent(this, RegisterUserActivity::class.java))
        finish()
    }
}