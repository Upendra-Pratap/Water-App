package com.example.waterapp.chatModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class GetDoChatResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("response")
    @Expose
    var response: List<Response>? = null

    inner class Response{
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("supportAgentId")
        @Expose
        var supportAgentId: String? = null

        @SerializedName("supportAgentName")
        @Expose
        var supportAgentName: String? = null

        @SerializedName("userId")
        @Expose
        var userId: String? = null

        @SerializedName("userName")
        @Expose
        var userName: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("attachment")
        @Expose
        var attachment: List<Any>? = null

        @SerializedName("isAdmin")
        @Expose
        var isAdmin: Int? = null

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