package com.example.waterapp.addBalanceModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class AddBalanceResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data{
        @SerializedName("address")
        @Expose
        var address: Address? = null

        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("user_name")
        @Expose
        var userName: String? = null

        @SerializedName("user_email")
        @Expose
        var userEmail: String? = null

        @SerializedName("password")
        @Expose
        var password: String? = null

        @SerializedName("profileImage")
        @Expose
        var profileImage: String? = null

        @SerializedName("phone_no")
        @Expose
        var phoneNo: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null

        @SerializedName("__v")
        @Expose
        var v: Int? = null

        @SerializedName("current_balance_water")
        @Expose
        var currentBalanceWater: Int? = null

        @SerializedName("current_balance_electricity")
        @Expose
        var currentBalanceElectricity: Int? = null

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