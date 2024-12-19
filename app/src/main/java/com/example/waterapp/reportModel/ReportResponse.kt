package com.example.waterapp.reportModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ReportResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("all_reports")
    @Expose
    var allReports: List<AllReport>? = null

    inner class AllReport{
        @SerializedName("user_name")
        @Expose
        var userName: String? = null

        @SerializedName("problemType")
        @Expose
        var problemType: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("date_of_incident")
        @Expose
        var dateOfIncident: String? = null

        @SerializedName("address")
        @Expose
        var address: String? = null

        @SerializedName("problem_photo")
        @Expose
        var problemPhoto: List<String>? = null
    }
}