package com.example.waterapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.databinding.ActivityServiceRequestBinding

class ServiceRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceRequestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}