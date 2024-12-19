package com.example.waterapp.announcementModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AnnouncementResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    inner class Datum{
        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("anncuncement_date")
        @Expose
        var anncuncementDate: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("anncuncementId")
        @Expose
        var anncuncementId: String? = null
    }
}