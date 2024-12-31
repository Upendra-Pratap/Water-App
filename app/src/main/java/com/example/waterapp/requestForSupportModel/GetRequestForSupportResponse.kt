package com.example.waterapp.requestForSupportModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class GetRequestForSupportResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("user_requests")
    @Expose
    var userRequests: List<UserRequest>? = null

    inner class UserRequest{
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("userId")
        @Expose
        var userId: String? = null

        @SerializedName("request_id")
        @Expose
        var requestId: String? = null

        @SerializedName("user_name")
        @Expose
        var userName: String? = null

        @SerializedName("serviceName")
        @Expose
        var serviceName: String? = null

        @SerializedName("issue_message")
        @Expose
        var issueMessage: String? = null

        @SerializedName("upload_photos")
        @Expose
        var uploadPhotos: List<String>? = null

        @SerializedName("address")
        @Expose
        var address: Address? = null

        @SerializedName("request_Date")
        @Expose
        var requestDate: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("contactDetails")
        @Expose
        var contactDetails: ContactDetails? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null

        @SerializedName("__v")
        @Expose
        var v: Int? = null

        inner class Address{
            @SerializedName("street")
            @Expose
            var street: String? = null

            @SerializedName("city")
            @Expose
            var city: String? = null

            @SerializedName("zip")
            @Expose
            var zip: String? = null
        }

        inner class ContactDetails{
            @SerializedName("email")
            @Expose
            var email: String? = null

            @SerializedName("phoneNumber")
            @Expose
            var phoneNumber: String? = null

        }
    }
}