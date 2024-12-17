package com.example.waterapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.databinding.ActivityViewBillBinding

class ViewBillActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewBillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBillBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}