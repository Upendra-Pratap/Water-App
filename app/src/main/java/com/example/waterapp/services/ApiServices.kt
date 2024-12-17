package com.example.waterapp.services

import com.example.waterapp.SignUpModel.SignUpResponse
import com.example.waterapp.forgotPasswordModel.ForgotPasswordBody
import com.example.waterapp.forgotPasswordModel.ForgotPasswordResponse
import com.example.waterapp.loginModel.LoginBody
import com.example.waterapp.loginModel.LoginResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiServices {
    @Multipart
    @Headers("Accept:application/json")
    @POST("user_signup")
    suspend fun registerUser(
        @Part("user_name") user_name: RequestBody,
        @Part("user_email") user_email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_no") phone_no: RequestBody,
        @Part("street") street: RequestBody,
        @Part("city") city: RequestBody,
        @Part("zip") zip: RequestBody,
        @Part profileImage: MultipartBody.Part,
    ): Response<SignUpResponse>

    @Headers("Accept:application/json")
    @POST("user_login")
    fun userLogin(
        @Body loginBody: LoginBody
    ): Observable<LoginResponse>

    @Headers("Accept:application/json")
    @POST("user_otpGenerate")
    fun forgotPassword(
        @Body forgotPasswordBody: ForgotPasswordBody
    ): Observable<ForgotPasswordResponse>

}