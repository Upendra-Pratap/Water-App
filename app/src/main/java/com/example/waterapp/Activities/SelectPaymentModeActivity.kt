package com.example.waterapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivitySelectPaymentModeBinding

class SelectPaymentModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectPaymentModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPaymentModeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.leftArrow.setOnClickListener { finish() }
    }
}