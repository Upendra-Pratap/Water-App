package com.example.waterapp.billpayment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BillPaymentResponse: Serializable {
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
        @SerializedName("transaction")
        @Expose
        var transaction: Transaction? = null

        @SerializedName("bill")
        @Expose
        var bill: Bill? = null

        inner class Transaction{
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

            @SerializedName("service_Type")
            @Expose
            var serviceType: String? = null

            @SerializedName("dueDate")
            @Expose
            var dueDate: String? = null

            @SerializedName("transactionStatus")
            @Expose
            var transactionStatus: String? = null

            @SerializedName("paymentMethod")
            @Expose
            var paymentMethod: String? = null

            @SerializedName("transactionType")
            @Expose
            var transactionType: String? = null

            @SerializedName("_id")
            @Expose
            var id: String? = null

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
        inner class Bill{
            @SerializedName("billing_period")
            @Expose
            var billingPeriod: BillingPeriod? = null

            @SerializedName("_id")
            @Expose
            var id: String? = null

            @SerializedName("userId")
            @Expose
            var userId: String? = null

            @SerializedName("service")
            @Expose
            var service: String? = null

            @SerializedName("service_type")
            @Expose
            var serviceType: String? = null

            @SerializedName("unit_consumed")
            @Expose
            var unitConsumed: Int? = null

            @SerializedName("total_amount")
            @Expose
            var totalAmount: Int? = null

            @SerializedName("due_date")
            @Expose
            var dueDate: String? = null

            @SerializedName("bill_status")
            @Expose
            var billStatus: String? = null

            @SerializedName("createdAt")
            @Expose
            var createdAt: String? = null

            @SerializedName("updatedAt")
            @Expose
            var updatedAt: String? = null

            @SerializedName("__v")
            @Expose
            var v: Int? = null

            @SerializedName("bill_pdf")
            @Expose
            var billPdf: String? = null

            inner class BillingPeriod{
                @SerializedName("start_date")
                @Expose
                var startDate: String? = null

                @SerializedName("end_date")
                @Expose
                var endDate: String? = null
            }
        }
    }
}