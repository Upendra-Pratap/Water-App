package com.example.waterapp.loginModel

import com.google.gson.annotations.SerializedName

class LoginBody(
    @SerializedName("user_email") var user_email: String,
    @SerializedName("password") var password: String,
    @SerializedName("phone_token") var phone_token: String
)