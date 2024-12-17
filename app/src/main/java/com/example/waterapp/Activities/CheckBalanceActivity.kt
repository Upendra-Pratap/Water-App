package com.example.waterapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.databinding.ActivityCheckBalanceBinding

class CheckBalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckBalanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckBalanceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

    }
}