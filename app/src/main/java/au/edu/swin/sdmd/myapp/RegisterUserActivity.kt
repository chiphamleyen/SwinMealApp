package au.edu.swin.sdmd.myapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import au.edu.swin.sdmd.myapp.datamodels.Customer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import services.MenuItemServices
import services.UserDbHelper
import java.util.*

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var fullNameTIL: TextInputEditText
    private lateinit var emailTIL: TextInputEditText
    private lateinit var employeeIDTIL: TextInputEditText
    private lateinit var mobileNumberTIL: TextInputEditText
    private lateinit var createPasswordTIL: TextInputEditText
    private lateinit var confirmPasswordTIL: TextInputEditText

    private lateinit var agreeCheckBox: CheckBox
    private lateinit var registerBtn: Button

    private val menuItemServices = MenuItemServices()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        fullNameTIL = findViewById(R.id.fullname)
        emailTIL = findViewById(R.id.email)
//        employeeIDTIL = findViewById(R.id.employeeId)
//        mobileNumberTIL = findViewById(R.id.mobile)
        createPasswordTIL = findViewById(R.id.password)
        confirmPasswordTIL = findViewById(R.id.cfpassword)

        agreeCheckBox = findViewById(R.id.register_agree_check_box)
        registerBtn = findViewById(R.id.register_emp_btn)

        agreeCheckBox.setOnClickListener {
            registerBtn.isEnabled = agreeCheckBox.isChecked
        }

        registerBtn.setOnClickListener {
//            val email = emailTIL.text.toString()
//            val password = createPasswordTIL.text.toString()
//            val cfpassword = confirmPasswordTIL.text.toString()
//            val name = fullNameTIL.text.toString()
//            val employeeId = employeeIDTIL.text.toString()
//            val mobile = mobileNumberTIL.text.toString()

            registerUser()
        }
    }

    //validation
    private fun registerUser() {
        val email = emailTIL.text.toString()
        val password = createPasswordTIL.text.toString()
        val cfpassword = confirmPasswordTIL.text.toString()
        val name = fullNameTIL.text.toString()

        val customer = Customer(UUID(12,1), email, password, name, null, null,null,null,null,null)

        if (password == cfpassword) {
            menuItemServices.registerUser(customer)
        }
//        val databaseHelper = UserDbHelper(this)
//        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
//        } else {
//            if (password == confirmPassword) {
//                databaseHelper.registerUser(name, email, password)
//
//                // Registration successful
//                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
//                // Redirect to login activity
//                val loginIntent = Intent(this, LoginUserActivity::class.java)
//                startActivity(loginIntent)
//                finish()
//                Log.i("user", name + email)
//
//            } else {
//                // Password and confirm password do not match
//                Toast.makeText(
//                    this,
//                    "Password and Confirm Password do not match",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
    }

    //login text
    fun openLoginActivity(view: View) {
        startActivity(Intent(this, LoginUserActivity::class.java))
        finish()
    }
}