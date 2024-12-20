package com.example.waterapp.notificationModel.CountNotificationModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class NotificationCountResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("all_notification_count")
    @Expose
    var allNotificationCount: Int? = null
}