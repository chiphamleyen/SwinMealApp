package au.edu.swin.sdmd.swinmealapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.datamodels.Customer
import com.google.android.material.textfield.TextInputEditText
import au.edu.swin.sdmd.swinmealapp.services.CustomerServices
import java.util.*

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var fullNameTIL: TextInputEditText
    private lateinit var emailTIL: TextInputEditText
    private lateinit var createPasswordTIL: TextInputEditText
    private lateinit var confirmPasswordTIL: TextInputEditText

    private lateinit var agreeCheckBox: CheckBox
    private lateinit var registerBtn: Button

    private val customerServices = CustomerServices()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        fullNameTIL = findViewById(R.id.fullname)
        emailTIL = findViewById(R.id.email)
        createPasswordTIL = findViewById(R.id.password)
        confirmPasswordTIL = findViewById(R.id.cfpassword)

        agreeCheckBox = findViewById(R.id.register_agree_check_box)
        registerBtn = findViewById(R.id.register_emp_btn)

        agreeCheckBox.setOnClickListener {
            registerBtn.isEnabled = agreeCheckBox.isChecked
        }

        registerBtn.setOnClickListener {
            registerUser()
        }
    }

    //validation
    private fun registerUser() {
        val email = emailTIL.text.toString()
        val password = createPasswordTIL.text.toString()
        val cfpassword = confirmPasswordTIL.text.toString()
        val name = fullNameTIL.text.toString()

        val customer = Customer(email, password, name, null, null,null,null,null)
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cfpassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }

        else {
            if (password == cfpassword) {
                customerServices.registerUser(customer) { response, errorMessage ->
                    if (response.isSuccessful) {
                        // Registration successful
                        runOnUiThread {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT)
                                .show()
                            val loginIntent = Intent(this, LoginUserActivity::class.java)
                            startActivity(loginIntent)
                            finish()
                        }
                    } else {
                        // Registration failed
                        runOnUiThread {
                            if (errorMessage != null) {
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            } else {
                // Password and confirm password do not match
                Toast.makeText(
                    this,
                    "Password and Confirm Password do not match",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //login text
    fun openLoginActivity(view: View) {
        startActivity(Intent(this, LoginUserActivity::class.java))
        finish()
    }
}