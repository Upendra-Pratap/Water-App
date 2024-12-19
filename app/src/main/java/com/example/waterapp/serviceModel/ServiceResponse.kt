package com.example.waterapp.serviceModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ServiceResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("All_Services")
    @Expose
    var allServices: List<AllService>? = null

    inner class AllService {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("service_type")
        @Expose
        var serviceType: String? = null

        @SerializedName("rate_per_unit")
        @Expose
        var ratePerUnit: Int? = null

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
    }
}