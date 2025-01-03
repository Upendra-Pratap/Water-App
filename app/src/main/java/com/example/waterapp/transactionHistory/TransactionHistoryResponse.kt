package com.example.waterapp.transactionHistory

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TransactionHistoryResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    inner class Datum {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("billId")
        @Expose
        var billId: String? = null

        @SerializedName("userId")
        @Expose
        var userId: String? = null

        @SerializedName("serviceProviderId")
        @Expose
        var serviceProviderId: String? = null

        @SerializedName("amount")
        @Expose
        var amount: Int? = null

        @SerializedName("dueDate")
        @Expose
        var dueDate: String? = null

        @SerializedName("service_Type")
        @Expose
        var serviceType: String? = null

        @SerializedName("transactionStatus")
        @Expose
        var transactionStatus: String? = null

        @SerializedName("paymentMethod")
        @Expose
        var paymentMethod: String? = null

        @SerializedName("transactionDate")
        @Expose
        var transactionDate: String? = null

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