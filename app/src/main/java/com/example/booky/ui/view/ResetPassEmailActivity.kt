package com.example.booky.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.booky.R
import com.example.booky.data.api.RestApiService
import com.example.booky.data.api.RetrofitInstance
import com.example.booky.data.models.User
import com.example.booky.utils.LoadingDialog
import com.google.android.material.textfield.TextInputLayout
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback


class ResetPassEmailActivity : AppCompatActivity() {


    lateinit var userEmailLayout: TextInputLayout;
    lateinit var userEmailEditText: EditText;
    lateinit var loadingDialog: LoadingDialog
    lateinit var submitBtn : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass_email)

        loadingDialog = LoadingDialog(this)
        userEmailLayout = findViewById(R.id.userEmail_tfLayout)
        userEmailEditText = findViewById(R.id.userEmailEditText)
        submitBtn = findViewById<Button>(R.id.submit_email_btn)



        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()




        submitBtn.setOnClickListener{

            if(validateInput(userEmailEditText)){

                SendConfirmCode(userEmailEditText.text.toString().trim())
                editor.putString("email_reset_pass",userEmailEditText.text.toString().trim() )
                editor.apply()


            }

        }
    }
    private fun SendConfirmCode(email:String){
        loadingDialog.startLoadingDialog()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.ResetPassEmail(User(email,null)).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@ResetPassEmailActivity,
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
                    Toast.makeText(this@ResetPassEmailActivity, "Email Sent!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ResetPassEmailActivity, ConfirmAfterResetActivity::class.java)
                    startActivity(intent)
                }
                else if (response.code()==404){
                    Toast.makeText(
                        this@ResetPassEmailActivity,
                        "User does not Exists...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@ResetPassEmailActivity,
                        "Activation failed failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }
    private fun validateInput( email: EditText): Boolean {
        if ( email.text.trim().isEmpty()) {


            if (email.text.isEmpty()) {
                email.error = "Email is required"
                email.requestFocus()

            }

        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()){
            email.error = "Email unvalid"
            email.requestFocus()
            return false
        }

        return true
    }
}

