package com.example.waterapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waterapp.databinding.ActivityAnnouncementBinding
import com.example.waterapp.adapter.AnnouncementAdapter


class AnnouncementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnnouncementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backArrow.setOnClickListener { finish() }

        binding.announcementRecyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = AnnouncementAdapter(this)
        binding.announcementRecyclerView.adapter = adapter

    }
}