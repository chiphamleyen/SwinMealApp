package au.edu.swin.sdmd.swinmealapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import au.edu.swin.sdmd.swinmealapp.R
import com.google.android.material.textfield.TextInputEditText
import au.edu.swin.sdmd.swinmealapp.services.CustomerServices

class LoginUserActivity : AppCompatActivity() {

    private lateinit var emailTIL: TextInputEditText
    private lateinit var passwordTIL: TextInputEditText
    private lateinit var btnLogin: Button
    private val customerServices = CustomerServices()

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
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            customerServices.customerLogin(email, password) { response, errorMessage ->
                if (response.isSuccessful) {
                    // Registration successful
                    runOnUiThread {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val loginIntent = Intent(this, MainActivity::class.java)
                        startActivity(loginIntent)

                        // Save email data to SharedPreferences
                        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor.putString("email", email)
                        editor.apply()


                        finish()
                    }
                } else {
                    // Registration failed
                    runOnUiThread {
                        if (errorMessage != null) {
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to login user", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    fun openRegisterActivity(view: View) {
        startActivity(Intent(this, RegisterUserActivity::class.java))
        finish()
    }
}