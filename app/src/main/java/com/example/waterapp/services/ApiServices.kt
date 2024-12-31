package com.example.waterapp.services

import com.example.waterapp.FaqModel.FaqResponse
import com.example.waterapp.SignUpModel.SignUpResponse
import com.example.waterapp.announcementModel.AnnouncementResponse
import com.example.waterapp.changePasswordModel.ChangePasswordBody
import com.example.waterapp.changePasswordModel.ChangePasswordResponse
import com.example.waterapp.chatModel.DoChatBody
import com.example.waterapp.chatModel.DoChatResponse
import com.example.waterapp.chatModel.GetDoChatResponse
import com.example.waterapp.forgotPasswordModel.ForgotPasswordBody
import com.example.waterapp.forgotPasswordModel.ForgotPasswordResponse
import com.example.waterapp.generateReportModel.GenerateReportResponse
import com.example.waterapp.loginModel.LoginBody
import com.example.waterapp.loginModel.LoginResponse
import com.example.waterapp.notificationModel.DeleteNotificationModel.DeleteNotificationResponse
import com.example.waterapp.notificationModel.CountNotificationModel.NotificationCountResponse
import com.example.waterapp.notificationModel.NotificationResponse
import com.example.waterapp.notificationModel.allNotificationDelete.AllNotificationDeleteResponse
import com.example.waterapp.otpVerificationModel.OtpVerificationResponse
import com.example.waterapp.otpVerificationModel.OtpVerifiicationBody
import com.example.waterapp.reportModel.ReportResponse
import com.example.waterapp.requestForSupportModel.GetRequestForSupportResponse
import com.example.waterapp.resetPasswordModel.ResetPasswordBody
import com.example.waterapp.resetPasswordModel.ResetPasswordResponse
import com.example.waterapp.serviceModel.ServiceResponse
import com.example.waterapp.transactionHistory.TransactionHistoryResponse
import com.example.waterapp.updateProfileModel.GetUpdateProfileResponse
import com.example.waterapp.updateProfileModel.UpdateProfileResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @Multipart
    @Headers("Accept:application/json")
    @PUT("update_user_details/{Id}")
    suspend fun updateProfile(
        @Part("user_name") user_name: RequestBody,
        @Part("user_email") user_email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_no") phone_no: RequestBody,
        @Part("street") street: RequestBody,
        @Part("city") city: RequestBody,
        @Part("zip") zip: RequestBody,
        @Part profileImage: MultipartBody.Part,
        @Path("Id") id: String
    ): Response<UpdateProfileResponse>

    @Headers("Accept:application/json")
    @POST("user_otpGenerate")
    fun forgotPassword(
        @Body forgotPasswordBody: ForgotPasswordBody
    ): Observable<ForgotPasswordResponse>

    @Headers("Accept:application/json")
    @POST("user_change_password/{Id}")
    fun changePassword(
        @Path("Id") id: String,
        @Body changePasswordBody: ChangePasswordBody
    ): Observable<ChangePasswordResponse>

    @Headers("Accept:application/json")
    @POST("user_verify_otp")
    fun otpVerification(
        @Body otpVerifiicationBody: OtpVerifiicationBody
    ): Observable<OtpVerificationResponse>

    @Headers("Accept:application/json")
    @POST("user_reset_password/{Id}")
    fun resetPassword(
        @Body resetPasswordBody: ResetPasswordBody,
        @Path("Id") id: String
    ): Observable<ResetPasswordResponse>

    @Headers("Accept:application/json")
    @GET("get_all_FAQ")
    fun getFaq(
    ): Observable<FaqResponse>

    @Headers("Accept:application/json")
    @GET("get_all_services")
    fun getServices(
    ): Observable<ServiceResponse>

    @Headers("Accept:application/json")
    @GET("get_all_announcemnets")
    fun getAnnouncement(
    ): Observable<AnnouncementResponse>

    @Headers("Accept:application/json")
    @GET("all_notification_of_user/{Id}")
    fun notificationList(
       @Path("Id") id: String
    ): Observable<NotificationResponse>

    @Headers("Accept:application/json")
    @GET("get_user_all_reports_for_problem/{Id}")
    fun getReport(
        @Path("Id") id: String
    ): Observable<ReportResponse>

    @Multipart
    @Headers("Accept:application/json")
    @POST("report_problem/{Id}")
    suspend fun generateReport(
        @Path("Id") id: String,
        @Part("problemType") problemType: RequestBody,
        @Part("description") description: RequestBody,
        @Part("date_of_incident") date_of_incident: RequestBody,
        @Part("street") street: RequestBody,
        @Part("city") city: RequestBody,
        @Part("zip") zip: RequestBody,
        @Part reportImages: MultipartBody.Part,

        ): Response<GenerateReportResponse>

    @Headers("Accept:application/json")
    @GET("get_user_data_by_id/{Id}")
    fun getUpdateProfile(
        @Path("Id") id: String
    ): Observable<GetUpdateProfileResponse>

    @Headers("Accept:application/json")
    @GET("all_notification_count_of_user/{Id}")
    fun notificationCount(
        @Path("Id") id: String
    ): Observable<NotificationCountResponse>

    @Headers("Accept:application/json")
    @DELETE("delete_notification/{Id}")
    fun deleteNotification(
        @Path("Id") id: String
    ): Observable<DeleteNotificationResponse>

    @Headers("Accept:application/json")
    @POST("request_for_address_update/{Id}")
    fun addressUpdate(
        @Path("Id") id: String
    ): Observable<NotificationResponse>

    @Headers("Accept:application/json")
    @DELETE("delete_all_notification_by_userId/{Id}")
    fun deleteAllNotification(
        @Path("Id") id: String
    ): Observable<AllNotificationDeleteResponse>

    @Headers("Accept:application/json")
    @POST("chat_support")
    fun  doChat(
        @Body doChatBody: DoChatBody
    ): Observable<DoChatResponse>

    @Headers("Accept:application/json")
    @POST("get_chat_support_by_id/{Id}")
    fun getDoChat(
        @Path("Id") id: String
    ): Observable<GetDoChatResponse>

    @Headers("Accept:application/json")
    @GET("get_user_requests_for_support/{Id}")
    fun getRequestForSupport(
        @Path("Id") id: String
    ): Observable<GetRequestForSupportResponse>

    @Headers("Accept:application/json")
    @GET("transaction_history/{Id}")
    fun transactionHistory(
        @Path("Id") id: String
    ): Observable<TransactionHistoryResponse>

}