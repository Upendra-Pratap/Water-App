package com.example.waterapp.billWaterElectricity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BillElectricityResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("Bill")
    @Expose
    var bill: List<Bill>? = null

    inner class Bill{
        @SerializedName("service_type")
        @Expose
        var serviceType: String? = null

        @SerializedName("Bill_period")
        @Expose
        var billPeriod: BillPeriod? = null

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

        @SerializedName("Bill")
        @Expose
        var bill: String? = null

        inner class BillPeriod{
            @SerializedName("start_date")
            @Expose
            var startDate: String? = null

            @SerializedName("end_date")
            @Expose
            var endDate: String? = null
        }
    }
}