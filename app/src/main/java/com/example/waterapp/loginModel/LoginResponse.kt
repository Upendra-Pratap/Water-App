package com.example.waterapp.loginModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class LoginResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("user_details")
    @Expose
    var userDetails: UserDetails? = null

    inner class UserDetails{
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("user_name")
        @Expose
        var userName: String? = null

        @SerializedName("user_email")
        @Expose
        var userEmail: String? = null

        @SerializedName("phone_no")
        @Expose
        var phoneNo: String? = null

        @SerializedName("profileImage")
        @Expose
        var profileImage: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("phone_token")
        @Expose
        var phoneToken: String? = null

        @SerializedName("address")
        @Expose
        var address: Address? = null

        @SerializedName("adminId")
        @Expose
        var adminId: String? = null
        inner class Address{
            @SerializedName("street")
            @Expose
            var street: String? = null

            @SerializedName("city")
            @Expose
            var city: String? = null

            @SerializedName("zip")
            @Expose
            var zip: Int? = null
        }
    }
}