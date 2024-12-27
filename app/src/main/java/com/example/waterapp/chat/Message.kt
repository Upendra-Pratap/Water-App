package com.example.waterapp.chat

class Message (
    var text: String? = null,
    var imageUrl: String? = null,
    var voiceUrl: String? = null,
    var senderId: String? = null,
    var timestamp: Long = System.currentTimeMillis()
)