package com.example.booky.ui.view

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
import com.example.booky.utils.LoadingDialog
import com.google.android.material.textfield.TextInputLayout
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class ConfirmAfterResetActivity : AppCompatActivity() {
    lateinit var userNewConfirmationCodeLayout: TextInputLayout;
    lateinit var userNewConfirmationCodeEditText: EditText;
    lateinit var loadingDialog: LoadingDialog
    lateinit var submitBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_after_reset)

        loadingDialog = LoadingDialog(this)
        userNewConfirmationCodeLayout = findViewById(R.id.Code_tfLayout)
        userNewConfirmationCodeEditText = findViewById(R.id.CodeEditText)
        submitBtn = findViewById<Button>(R.id.Nsubmit_btn)
        submitBtn.setOnClickListener{

            if(validateInput(userNewConfirmationCodeEditText)){
                Confirm(userNewConfirmationCodeEditText.text.toString().trim())
            }

        }
    }
    private fun Confirm(code:String){
        loadingDialog.startLoadingDialog()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.NewverifyUser(User(code)).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@ConfirmAfterResetActivity,
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
                    Toast.makeText(this@ConfirmAfterResetActivity, "All Done!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ConfirmAfterResetActivity, ResetPasswordActivity::class.java)
                    startActivity(intent)
                }
                else if (response.code()==404){
                    Toast.makeText(
                        this@ConfirmAfterResetActivity,
                        "User does not Exists...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@ConfirmAfterResetActivity,
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