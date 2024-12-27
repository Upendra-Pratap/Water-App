package com.example.waterapp.chatModel

import com.google.gson.annotations.SerializedName

class DoChatBody(
    @SerializedName("supportAgentId") var supportAgentId :String,
    @SerializedName("userId") var userId :String,
    @SerializedName("message") var message :String,
)