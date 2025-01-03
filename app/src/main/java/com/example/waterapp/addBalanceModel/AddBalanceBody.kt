package com.example.waterapp.addBalanceModel

import com.google.gson.annotations.SerializedName

class AddBalanceBody(
    @SerializedName("topupType") val topupType: Int,
    @SerializedName("balance") val balance: Int
)