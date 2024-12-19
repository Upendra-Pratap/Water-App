package com.example.waterapp.FaqModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class FaqResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("faq")
    @Expose
    var faq: List<Faq>? = null

    inner class Faq {
        @SerializedName("Question")
        @Expose
        var question: String? = null

        @SerializedName("Answer")
        @Expose
        var answer: String? = null

        @SerializedName("faq_id")
        @Expose
        var faqId: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null
    }
}