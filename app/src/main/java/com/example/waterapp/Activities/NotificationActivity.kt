package com.example.waterapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivityNotificationBinding
import com.example.waterapp.adapter.NotificationAdapter

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerNotification)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = NotificationAdapter(this)
        recyclerView.adapter = adapter
    }
}