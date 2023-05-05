package com.example.booky.data.api
import com.example.booky.data.models.LoginResponse
import com.example.booky.data.models.User
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RestApiService {

    //*********************** Sign up/in ***********************//

    @Headers("Content-Type:application/json")
    @POST("user/login")
    fun loginUser(@Body info: User): Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("user/register")
    fun registerUser(
        @Body info: User
    ): Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("user/verify")
    fun verifyUser(
        @Body info: User
    ): Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("user/newverify")
    fun NewverifyUser(
        @Body info: User
    ): Call<ResponseBody>



    @Headers("Content-Type:application/json")
    @POST("user/reset")
    fun ResetPassEmail(
        @Body info: User
    ): Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @PATCH("user/newpassemail")
    fun UpdatePass(
        @Body info: User
    ): Call<ResponseBody>



}


class RetrofitInstance {
    companion object {


        const val BASE_URL: String = "http://10.0.2.2:9090/"

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}