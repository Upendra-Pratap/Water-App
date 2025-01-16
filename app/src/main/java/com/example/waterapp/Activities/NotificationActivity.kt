package com.example.waterapp.Activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivityNotificationBinding
import com.example.waterapp.adapter.NotificationAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.notificationModel.DeleteNotificationModel.DeleteNotificationViewModel
import com.example.waterapp.notificationModel.CountNotificationModel.NotificationCountViewModel
import com.example.waterapp.notificationModel.NotificationResponse
import com.example.waterapp.notificationModel.NotificationViewModel
import com.example.waterapp.notificationModel.allNotificationDelete.AllNotificationDeleteVewModel
import com.example.waterapp.notificationModel.seeNotification.SeeNotificationViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity(), NotificationClickListener {
    private lateinit var binding: ActivityNotificationBinding
    private val notificationViewModel: NotificationViewModel by viewModels()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()
    private val deleteNotificationViewModel: DeleteNotificationViewModel by viewModels()
    private val allNotificationDeleteVewModel: AllNotificationDeleteVewModel by viewModels()
    private val seeNotificationViewModel: SeeNotificationViewModel by viewModels()
    private var myOrderAdapter: NotificationAdapter? = null
    private var notificationList: List<NotificationResponse.AllNotification> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var userId = ""
    private var id =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()
        id = sharedPreferences.getString("id", id).toString().trim()

        binding.arrowBack.setOnClickListener { finish() }

        binding.allClear.setOnClickListener { allDeletePopup() }

        //Observer and api
        notificationListApi(userId)
        notificationObserver()
        notificationCountObserver()
        deleteNotificationObserver()
        seeNotificationObserver()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun seeNotificationObserver() {
        seeNotificationViewModel.progressIndicator.observe(this){

        }
        seeNotificationViewModel.mRejectResponse.observe(this){
            val status = it.peekContent().success
            val message = it.peekContent().message
            if (status == true){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        seeNotificationViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun allNotificationDeleteObserver() {
        allNotificationDeleteVewModel.progressIndicator.observe(this){

        }
        allNotificationDeleteVewModel.mDeleteNotificationResponse.observe(this){
            val status = it.peekContent().success
            val message = it.peekContent().message
            if (status == true){
                Toast.makeText(this@NotificationActivity, message, Toast.LENGTH_SHORT).show()
                progressDialog.stop()
                notificationListApi(userId)
                notificationCountApi(userId)

            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        allNotificationDeleteVewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allDeletePopup() {
            val builder = AlertDialog.Builder(this, R.style.Style_Dialog_Rounded_Corner)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_notification_popup, null)
            builder.setView(dialogView)

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val noBtnAcceptNDel = dialogView.findViewById<TextView>(R.id.NoBtnAcceptNDel)
            val yesBtnAcceptNDel = dialogView.findViewById<TextView>(R.id.YesBtnAcceptNdel)

            noBtnAcceptNDel.setOnClickListener {
                dialog.dismiss()
            }

            yesBtnAcceptNDel.setOnClickListener {
                //calling api all notification delete
                allNotificationDeleteApi(userId)
                allNotificationDeleteObserver()
                myOrderAdapter?.notifyDataSetChanged()
                dialog.dismiss()
            }

            dialog.show()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun allNotificationDeleteApi(userId: String) {
        allNotificationDeleteVewModel.allNotificationDelete(userId, progressDialog, this)
    }

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun deleteNotificationObserver() {
        deleteNotificationViewModel.progressIndicator.observe(this){

        }

        deleteNotificationViewModel.mDeleteNotificationResponse.observe(this) { response ->
            val message = response.peekContent().message!!
            val status = response.peekContent().success

            if(status == true) {
                notificationListApi(userId)
                notificationCountApi(userId)
                myOrderAdapter!!.notifyDataSetChanged()
            }else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()

            }
        }

        deleteNotificationViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this) {

        }
        notificationCountViewModel.mCustomerResponse.observe(this) {
            val status = it.peekContent().success
            val message = it.peekContent().message
            val notificationCount = it.peekContent().allNotificationCount

            if (status == true) {
                binding.notificationCount.text = notificationCount.toString()
                myOrderAdapter!!.notifyDataSetChanged()
            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        notificationCountViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun notificationObserver() {
        notificationViewModel.progressIndicator.observe(this, androidx.lifecycle.Observer {
            // Show progress if needed
        })
        notificationViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success
            notificationList = it.peekContent().allNotification!!

            if (status == true) {
                if (notificationList.isNotEmpty()) {
                    binding.recyclerNotification.isVerticalScrollBarEnabled = true
                    binding.recyclerNotification.isVerticalFadingEdgeEnabled = true
                    binding.recyclerNotification.layoutManager = GridLayoutManager(this, 1)
                    myOrderAdapter = NotificationAdapter(this, this, notificationList)

                    binding.recyclerNotification.adapter = myOrderAdapter

                    notificationCountApi(userId)
                    myOrderAdapter!!.notifyDataSetChanged()

                }
            }else {

                }
            }

        notificationViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }
    private fun notificationCountApi(userId: String) {
        notificationCountViewModel.notificationCount(userId, progressDialog, this)
    }

    private fun notificationListApi(userId: String) {
        notificationViewModel.notificationList(userId, this, progressDialog)
    }

    override fun deleteNotification(position: Int, id: String) {
        deleteNotificationApi(id)
    }

    override fun seeNotification(position: Int, id: String) {
        seenNotificationApi(id)
    }

    private fun seenNotificationApi(id: String) {
        seeNotificationViewModel.seeNotification(id, this, progressDialog)
    }

    private fun deleteNotificationApi(notificationId: String) {
        deleteNotificationViewModel.getDeleteNotifications(notificationId, progressDialog, this)

    }
    override fun onResume() {
        super.onResume()
        notificationListApi(userId)
    }
}