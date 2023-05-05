package com.example.booky.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.booky.R
import com.example.booky.data.api.RestApiService
import com.example.booky.data.api.RetrofitInstance
import com.example.booky.data.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import android.util.Log
import kotlin.math.log

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var submitBtn : Button
    lateinit var idUser : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        submitBtn = findViewById<Button>(R.id.submit_btn)
        submitBtn.setOnClickListener {
            val newPass = findViewById<EditText>(R.id.newpasswordEditText)
            val confNewPass = findViewById<EditText>(R.id.confnewpasswordEditText)
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val email = sharedPreferences.getString("email_reset_pass", null).toString()
            print(email)

            if (validateNewPass(newPass, confNewPass)) {
                UpdatePass(
                    //"hamzabouzidi1999@gmail.com",
                    email,
                    newPass.text.toString().trim(),

                    )
            }
        }
    }
    private fun validateNewPass(newPass: EditText,confNewPass: EditText): Boolean {
        if (newPass.text.trim().isEmpty() || confNewPass.text.trim().isEmpty() ) {


            if (newPass.text.isEmpty()) {
                newPass.error = "Password is required"
                newPass.requestFocus()

            }


            if (confNewPass.text.isEmpty()) {
                confNewPass.error = "Password does not match"
                confNewPass.requestFocus()

            }

            return false
        }

        //Patterns // Regex // Length
        if (newPass.text.length < 6){
            newPass.error = "Password must be at least 6 characters"
            newPass.requestFocus()
            return false
        }

        if (newPass.text.toString() != confNewPass.text.toString()){
            confNewPass.error = "Password does not match"
            confNewPass.requestFocus()
            return false
        }


        return true
    }

    private fun UpdatePass(email: String ,newPass: String) {

        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val UserInfo = User( "","","",email,newPass,null,"")

        retIn.UpdatePass(UserInfo).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@ResetPasswordActivity,
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
                    Toast.makeText(this@ResetPasswordActivity, "Password Updated!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                    startActivity(intent)
                }

                else{
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}