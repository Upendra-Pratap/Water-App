package com.example.waterapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivityRequestForSupportBinding

class RequestForSupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestForSupportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestForSupportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backArrow.setOnClickListener { finish() }

    }
}