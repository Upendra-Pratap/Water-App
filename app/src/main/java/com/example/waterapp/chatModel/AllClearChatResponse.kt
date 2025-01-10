package com.example.waterapp.chatModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class AllClearChatResponse: Serializable {
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
        @SerializedName("acknowledged")
        @Expose
        var acknowledged: Boolean? = null

        @SerializedName("deletedCount")
        @Expose
        var deletedCount: Int? = null
    }
}