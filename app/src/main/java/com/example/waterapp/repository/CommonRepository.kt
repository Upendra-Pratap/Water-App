package com.example.waterapp.repository

import io.reactivex.Observable
import com.example.waterapp.SignUpModel.SignUpResponse
import com.example.waterapp.forgotPasswordModel.ForgotPasswordBody
import com.example.waterapp.forgotPasswordModel.ForgotPasswordResponse
import com.example.waterapp.loginModel.LoginBody
import com.example.waterapp.loginModel.LoginResponse
import com.example.waterapp.services.ApiServices
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class CommonRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getCustomerRegister(
        user_name: RequestBody,
        user_email: RequestBody,
        password: RequestBody,
        phone_no: RequestBody,
        street: RequestBody,
        city: RequestBody,
        zip: RequestBody,
        profileImage: MultipartBody.Part
    ): Response<SignUpResponse> {
        return apiServices.registerUser(user_name, user_email,password, phone_no,street,city,zip, profileImage)
    }
    fun customerLogin(loginBody: LoginBody): Observable<LoginResponse> {
        return apiServices.userLogin(loginBody)
    }

    fun forgotPassword(forgotPasswordBody: ForgotPasswordBody): Observable<ForgotPasswordResponse>{
        return apiServices.forgotPassword(forgotPasswordBody)
    }

}