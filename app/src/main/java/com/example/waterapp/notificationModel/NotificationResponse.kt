package com.example.waterapp.notificationModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class NotificationResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("all_notification")
    @Expose
    var allNotification: List<AllNotification>? = null

    inner class AllNotification{
        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("Date")
        @Expose
        var date: String? = null

        @SerializedName("notification_id")
        @Expose
        var notificationId: String? = null
    }
}