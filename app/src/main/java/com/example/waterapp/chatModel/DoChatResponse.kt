package com.example.waterapp.chatModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class DoChatResponse: Serializable {
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
        var attachment: List<String>? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("__v")
        @Expose
        var v: Int? = null
    }
}