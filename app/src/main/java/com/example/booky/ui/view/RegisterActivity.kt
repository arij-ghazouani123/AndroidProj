package com.example.booky.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.booky.R
import com.example.booky.data.api.RestApiService
import com.example.booky.data.api.RetrofitInstance
import com.example.booky.data.models.User
import com.example.booky.utils.LoadingDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var loginbtn : Button
    lateinit var registerbtn : Button
   // lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerbtn = findViewById<Button>(R.id.signup_btn)
        registerbtn.setOnClickListener {
            val firstname = findViewById<EditText>(R.id.firstNameEditText)
            val lastname = findViewById<EditText>(R.id.lastNameEditText)
            val email = findViewById<EditText>(R.id.userEmailEditText)
            val password = findViewById<EditText>(R.id.passwordEditText)
            val verifPass = findViewById<EditText>(R.id.repeatPassEditText)

            if (validateRegister(firstname,lastname, email, password,verifPass)) {
                register(
                    firstname.text.toString().trim(),
                    lastname.text.toString().trim(),
                    email.text.toString().trim(),
                    password.text.toString().trim(),
                )}
        }

       // Redirect To Login Screen

       val loginrBtn = findViewById<TextView>(R.id.login_link)
       loginrBtn.setOnClickListener {
           val intent = Intent(this, LoginActivity::class.java)
           startActivity(intent)
       }

    }

    private fun validateRegister(firstname: EditText,lastname: EditText, email: EditText, password: EditText, verifPass: EditText): Boolean {
        if (firstname.text.trim().isEmpty() || lastname.text.trim().isEmpty() || email.text.trim().isEmpty() ||  password.text.trim().isEmpty() || verifPass.text.trim().isEmpty()) {


            if (email.text.isEmpty()) {
                email.error = "Email is required"
                email.requestFocus()

            }


            if (verifPass.text.isEmpty()) {
                verifPass.error = "Password does not match"
                verifPass.requestFocus()

            }

            if (password.text.isEmpty()) {
                password.error = "Password is required"
                password.requestFocus()

            }

            if (firstname.text.isEmpty()) {
                firstname.error = "first name is required"
                firstname.requestFocus()

            }
            if (lastname.text.isEmpty()) {
                lastname.error = "last name is required"
                lastname.requestFocus()

            }

            return false
        }

        //Patterns // Regex // Length
        if (password.text.length < 6){
            password.error = "Password must be at least 6 characters"
            password.requestFocus()
            return false
        }

        if (password.text.toString() != verifPass.text.toString()){
            verifPass.error = "Password does not match"
            verifPass.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()){
            email.error = "Email unvalid"
            email.requestFocus()
            return false
        }
        if (firstname.text.length < 3){
            firstname.error = "firstname must be at least 3 characters"
            firstname.requestFocus()
            return false
        }
        if (lastname.text.length < 3){
            lastname.error = "lastname must be at least 3 characters"
            lastname.requestFocus()
            return false
        }

       /* if (Pattern.compile("^[a-zA-Z ]+$").matcher(firstname.text).matches()){
            firstname.error = "Invalid First Name"
            firstname.requestFocus()
            return false
        }
        if (Pattern.compile("^[a-zA-Z ]+$").matcher(lastname.text).matches()){
            lastname.error = "Invalid Last Name"
            lastname.requestFocus()
            return false
        }
*/

        return true
    }

    private fun register(firstName: String, lastName: String, email: String, password: String,) {

        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val registerInfo = User( firstName, lastName, email, password)

        retIn.registerUser(registerInfo).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>

            ) {
                if (response.code() == 200) {
                    //loadingDialog.dismissDialog()
                    Toast.makeText(this@RegisterActivity, "User Added Successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, ComfirmAccountActivity::class.java)
                    startActivity(intent)
                    finish()
                    }

                else if (response.code()==409){
                    Toast.makeText(
                        this@RegisterActivity,
                        "User Already Exists. Please Login",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Register failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }


}