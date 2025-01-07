package com.example.waterapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivitySelectPaymentModeBinding

class SelectPaymentModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectPaymentModeBinding
    private var userType = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPaymentModeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userType = intent.getIntExtra("userType",0).toString()

        Toast.makeText(this, userType, Toast.LENGTH_SHORT).show()

        binding.leftArrow.setOnClickListener { finish() }
    }
}