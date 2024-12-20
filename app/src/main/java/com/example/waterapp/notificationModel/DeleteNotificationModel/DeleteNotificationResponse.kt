package com.example.waterapp.notificationModel.DeleteNotificationModel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class DeleteNotificationResponse {
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
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("userId")
        @Expose
        var userId: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("date")
        @Expose
        var date: String? = null

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