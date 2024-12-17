package com.example.waterapp.forgotPasswordModel

import com.google.gson.annotations.SerializedName

class ForgotPasswordBody(
    @SerializedName("user_email") var user_email: String
)