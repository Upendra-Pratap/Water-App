package com.example.waterapp.resetPasswordModel

import com.google.gson.annotations.SerializedName

class ResetPasswordBody(
    @SerializedName("newPassword") var newPassword: String,
    @SerializedName("confirmPassword") var confirmPassword: String
)