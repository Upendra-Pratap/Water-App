package com.example.waterapp.changePasswordModel

import com.google.gson.annotations.SerializedName

class ChangePasswordBody(
    @SerializedName("oldPassword") var oldPassword :String,
    @SerializedName("newPassword") var newPassword :String,
    @SerializedName("confirmNewPassword") var confirmNewPassword :String
)