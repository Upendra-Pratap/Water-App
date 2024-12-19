package com.example.waterapp.Activities

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.FaqModel.FaqResponse
import com.example.waterapp.R
import com.example.waterapp.adapter.AnnouncementAdapter
import com.example.waterapp.adapter.FaqAdapter
import com.example.waterapp.databinding.ActivityNotificationBinding
import com.example.waterapp.adapter.NotificationAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.notificationModel.NotificationResponse
import com.example.waterapp.notificationModel.NotificationViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val notificationViewModel: NotificationViewModel by viewModels()
    private var myOrderAdapter: NotificationAdapter? = null
    private var notificationList: List<NotificationResponse.AllNotification> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var activity: Activity
    private var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressDialog = CustomProgressDialog(this)
        activity = this

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        Toast.makeText(this, "user id here : $userId", Toast.LENGTH_SHORT).show()

        binding.arrowBack.setOnClickListener { finish() }


        notificationListApi(userId)
        notificationObserver()

    }

    private fun notificationObserver() {
        notificationViewModel.progressIndicator.observe(this, androidx.lifecycle.Observer {
            // Show progress if needed
        })

        notificationViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success
            val message = it.peekContent().message
            notificationList = it.peekContent().allNotification!!

            if (notificationList.isNotEmpty()) {

                binding.recyclerNotification.isVerticalScrollBarEnabled = true
                binding.recyclerNotification.isVerticalFadingEdgeEnabled = true
                binding.recyclerNotification.layoutManager = GridLayoutManager(this, 1)
                myOrderAdapter = NotificationAdapter(this, notificationList)

                // Set the adapter to RecyclerView
                binding.recyclerNotification.adapter = myOrderAdapter


            } else {

            }
        }

        notificationViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }


    private fun notificationListApi(userId: String) {
        notificationViewModel.notificationList(userId, activity, progressDialog)
    }
}