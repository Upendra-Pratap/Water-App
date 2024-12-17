package com.example.waterapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waterapp.databinding.ActivityFqaBinding
import com.example.waterapp.adapter.FaqAdapter

class FaqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFqaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFqaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.faqRecyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = FaqAdapter(this)
        binding.faqRecyclerView.adapter = adapter

        }
    }
