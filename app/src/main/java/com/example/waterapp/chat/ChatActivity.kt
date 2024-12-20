package com.example.waterapp.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

    }
}