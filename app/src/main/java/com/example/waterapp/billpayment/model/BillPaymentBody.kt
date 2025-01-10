package com.example.waterapp.billpayment.model

import com.google.gson.annotations.SerializedName

class BillPaymentBody(
    @SerializedName("transactionStatus") var transactionStatus: String,
    @SerializedName("paymentMethod") var paymentMethod: String,

)