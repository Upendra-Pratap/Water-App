package com.example.waterapp.Activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.databinding.ActivityAnnouncementBinding
import com.example.waterapp.adapter.AnnouncementAdapter
import com.example.waterapp.announcementModel.AnnouncementResponse
import com.example.waterapp.announcementModel.AnnouncementViewModel
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AnnouncementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnnouncementBinding
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var activity: Activity
    private var myOrderAdapter: AnnouncementAdapter? = null
    private var announcementList: List<AnnouncementResponse.Datum> = ArrayList()
    private val announcementViewModel: AnnouncementViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getAnnouncementApi()
        getAnnouncementObserver()

        binding.backArrow.setOnClickListener { finish() }


    }

    private fun getAnnouncementObserver() {
        announcementViewModel.progressIndicator.observe(this, androidx.lifecycle.Observer {
            // Show progress if needed
        })

        announcementViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success
            val message = it.peekContent().message
            announcementList = it.peekContent().data!!

            if (announcementList.isNotEmpty()) {

                binding.announcementRecyclerView.isVerticalScrollBarEnabled = true
                binding.announcementRecyclerView.isVerticalFadingEdgeEnabled = true
                binding.announcementRecyclerView.layoutManager = GridLayoutManager(this, 1)
                myOrderAdapter = AnnouncementAdapter(this, announcementList)

                // Set the adapter to RecyclerView
                binding.announcementRecyclerView.adapter = myOrderAdapter


            } else {

            }
        }

        announcementViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@AnnouncementActivity, it)
        }
    }


    private fun getAnnouncementApi() {
        announcementViewModel.getAnnouncement(this, progressDialog)
    }
}