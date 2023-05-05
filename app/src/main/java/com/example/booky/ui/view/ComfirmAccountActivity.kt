package com.example.booky.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.booky.MainActivity
import com.example.booky.R
import com.example.booky.data.api.RestApiService
import com.example.booky.data.api.RetrofitInstance
import com.example.booky.data.models.User
import com.example.booky.utils.LoadingDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class ComfirmAccountActivity : AppCompatActivity() {
    lateinit var userConfirmationCodeLayout: TextInputLayout;
    lateinit var userConfirmationCodeEditText: EditText;
    lateinit var loadingDialog: LoadingDialog

    lateinit var submitBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comfirm_account)

        loadingDialog = LoadingDialog(this)
        userConfirmationCodeLayout = findViewById(R.id.confirmationCode_tfLayout)
        userConfirmationCodeEditText = findViewById(R.id.ConfirmationCodeEditText)
        submitBtn = findViewById<Button>(R.id.submit_btn)
        submitBtn.setOnClickListener{

            if(validateInput(userConfirmationCodeEditText)){
                Confirm(userConfirmationCodeEditText.text.toString().trim())
            }

        }
    }


    private fun Confirm(code:String){
        loadingDialog.startLoadingDialog()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.verifyUser(User(code)).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@ComfirmAccountActivity,
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
                    //saving data
                    val gson = Gson()
                    val jsonSTRING = response.body()?.string()
                    val jsonObject = gson.fromJson(jsonSTRING, JsonObject::class.java)
                    val userId = jsonObject.get("id").asString
                    val userFirstName = jsonObject.get("firstName").asString
                    val userLastName = jsonObject.get("lastName").asString
                    val userEmail =jsonObject.get("email").asString
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userId",userId )
                    editor.putString("userFirstName",userFirstName )
                    editor.putString("userLastName",userLastName )
                    editor.putString("userEmail",userEmail )
                    editor.apply()
                    Toast.makeText(this@ComfirmAccountActivity, "Account Activated Successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ComfirmAccountActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else if (response.code()==404){
                    Toast.makeText(
                        this@ComfirmAccountActivity,
                        "User does not Exists...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@ComfirmAccountActivity,
                        "Activation failed failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }
    private fun validateInput(code: EditText): Boolean {
        if(code.text.trim().isEmpty()){

            if (code.text.isEmpty()) {
                code.error = "This field is required"
                code.requestFocus()

            }

            return false

        }

        return true
    }
}