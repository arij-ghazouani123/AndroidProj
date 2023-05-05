package com.example.booky.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.booky.MainActivity
import com.example.booky.R
import com.example.booky.data.api.RestApiService
import com.example.booky.data.api.RetrofitInstance
import com.example.booky.data.models.LoginResponse
import com.example.booky.data.models.User
import com.example.booky.utils.LoadingDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.newFixedThreadPoolContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var userEmailLayout: TextInputLayout;
    lateinit var passwordLayout: TextInputLayout;
    lateinit var userEmailEditText: EditText;
    lateinit var passwordEditText: EditText;
    lateinit var remember: CheckBox;
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val rememberState = sharedPreferences.getBoolean("remember",false)

        if (rememberState){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        loadingDialog = LoadingDialog(this)
        userEmailLayout = findViewById(R.id.userEmail_tfLayout)
        passwordLayout = findViewById(R.id.password_tfLayout)
        userEmailEditText = findViewById(R.id.userEmailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        remember = findViewById(R.id.rememberMe_ckbx)



        // Redirect To Register Screen

        val registerBtn = findViewById<TextView>(R.id.register_link)
       registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        // Redirect To New Activation Code Screen
        val forgetpassBtn = findViewById<TextView>(R.id.forgetpass_link)
        forgetpassBtn.setOnClickListener {
            val intent2 = Intent(this, ResetPassEmailActivity::class.java)
            startActivity(intent2)
        }


        // Login Button Handler

        val loginBtn = findViewById<Button>(R.id.login_btn)
        loginBtn.setOnClickListener() {
            if (validateLogin(userEmailEditText, passwordEditText,passwordLayout)) {

                login(userEmailEditText.text.trim().toString(), passwordEditText.text.trim().toString())

                //remember.setOnCheckedChangeListener(new


            }else{
                val toast = Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT)
                toast.show()
            }

        }




    }
    // Validate User Input
    private fun validateLogin(username: EditText, password: EditText,passwordlayout : TextInputLayout): Boolean {
        if(username.text.trim().isEmpty() || password.text.trim().isEmpty()){

            if (password.text.isEmpty()) {
                password.error = "Password is required"
                password.requestFocus()

            }
            // made it revesed so it desplays correctly you ll see it in the app
            if (username.text.isEmpty()) {
                username.error = "Username is required"
                username.requestFocus()

            }

            return false

        }

        return true
    }


    private fun login(email: String, password: String) {
        loadingDialog.startLoadingDialog()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val signInInfo = User(email, password)

        retIn.loginUser(signInInfo).enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loadingDialog.dismissDialog()
                Toast.makeText(
                    this@LoginActivity,
                    "Login Failed",
                    Toast.LENGTH_SHORT
                ).show()


            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    loadingDialog.dismissDialog()
                    //saving data
                    val gson = Gson()
                    val jsonSTRING = response.body()?.string()
                    val jsonObject = gson.fromJson(jsonSTRING, JsonObject::class.java)
                    val userId = jsonObject.get("id").asString
                    val userFirstName = jsonObject.get("firstName").asString
                    val userLastName = jsonObject.get("lastName").asString
                    val userEmail =userEmailEditText.text.trim().toString()
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userId",userId )
                    editor.putString("userFirstName",userFirstName )
                    editor.putString("userLastName",userLastName )
                    editor.putString("userEmail",userEmail )
                    editor.apply()
                    //val email = sharedPreferences.getString("userEmail", null).toString()
                    //val id = sharedPreferences.getString("userId", null).toString()
                    remember.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("remember",true )
                            editor.apply()

                        }
                    }








                    Toast.makeText(this@LoginActivity, "Welcome!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else if(response.code() == 404){
                    loadingDialog.dismissDialog()
                    Toast.makeText(this@LoginActivity, "Invalid Email", Toast.LENGTH_SHORT).show()
                }
                else if(response.code() == 201){
                    loadingDialog.dismissDialog()
                    Toast.makeText(this@LoginActivity, "Please Verify Your Email!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, ComfirmAccountActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    loadingDialog.dismissDialog()
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

}