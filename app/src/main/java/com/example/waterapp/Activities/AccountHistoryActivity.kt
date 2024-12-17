package com.example.waterapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.databinding.ActivityAccountHistoryBinding


class AccountHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backArrow.setOnClickListener { finish() }

    }
}