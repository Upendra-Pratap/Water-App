package com.example.waterapp.repository

import com.example.waterapp.FaqModel.FaqResponse
import io.reactivex.Observable
import com.example.waterapp.SignUpModel.SignUpResponse
import com.example.waterapp.announcementModel.AnnouncementResponse
import com.example.waterapp.changePasswordModel.ChangePasswordBody
import com.example.waterapp.changePasswordModel.ChangePasswordResponse
import com.example.waterapp.forgotPasswordModel.ForgotPasswordBody
import com.example.waterapp.forgotPasswordModel.ForgotPasswordResponse
import com.example.waterapp.generateReportModel.GenerateReportResponse
import com.example.waterapp.loginModel.LoginBody
import com.example.waterapp.loginModel.LoginResponse
import com.example.waterapp.notificationModel.NotificationResponse
import com.example.waterapp.otpVerificationModel.OtpVerificationResponse
import com.example.waterapp.otpVerificationModel.OtpVerifiicationBody
import com.example.waterapp.reportModel.ReportResponse
import com.example.waterapp.resetPasswordModel.ResetPasswordBody
import com.example.waterapp.resetPasswordModel.ResetPasswordResponse
import com.example.waterapp.serviceModel.ServiceResponse
import com.example.waterapp.services.ApiServices
import com.example.waterapp.updateProfileModel.UpdateProfileResponse
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

    suspend fun updateProfile(
        user_name: RequestBody,
        user_email: RequestBody,
        password: RequestBody,
        phone_no: RequestBody,
        street: RequestBody,
        city: RequestBody,
        zip: RequestBody,
        profileImage: MultipartBody.Part,
        id: String
    ): Response<UpdateProfileResponse>{
        return apiServices.updateProfile(user_name, user_email, password, phone_no, street, city, zip, profileImage, id)
    }

    fun generateReport(
        id: String,
        problemType: RequestBody,
        description: RequestBody,
        date_of_incident: RequestBody,
        street: RequestBody,
        city: RequestBody,
        zip: RequestBody,
        reportImages: MultipartBody.Part,

    ): Response<GenerateReportResponse>{
        return apiServices.generateReport(id, problemType, description, date_of_incident, street, city, zip, reportImages)
    }

    fun forgotPassword(forgotPasswordBody: ForgotPasswordBody): Observable<ForgotPasswordResponse>{
        return apiServices.forgotPassword(forgotPasswordBody)
    }
    fun changePassword(id:String, changePasswordBody: ChangePasswordBody): Observable<ChangePasswordResponse>{
        return apiServices.changePassword(id, changePasswordBody)
    }

    fun otpVerification(otpVerifiicationBody: OtpVerifiicationBody): Observable<OtpVerificationResponse>{
        return apiServices.otpVerification(otpVerifiicationBody)
    }

    fun resetPassword(resetPasswordBody: ResetPasswordBody, id: String): Observable<ResetPasswordResponse>{
        return apiServices.resetPassword(resetPasswordBody, id)
    }

    fun getFaq(): Observable<FaqResponse>{
        return apiServices.getFaq()
    }

    fun getService(): Observable<ServiceResponse>{
        return apiServices.getServices()
    }

    fun getAnnouncement(): Observable<AnnouncementResponse>{
        return apiServices.getAnnouncement()
    }

    fun notificationList(id: String): Observable<NotificationResponse>{
        return apiServices.notificationList(id)
    }

    fun getReport(id: String): Observable<ReportResponse>{
        return apiServices.getReport(id)
    }

}