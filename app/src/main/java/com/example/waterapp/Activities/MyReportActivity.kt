package com.example.waterapp.Activities

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.adapter.ReportAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityMyReportBinding
import com.example.waterapp.reportModel.ReportResponse
import com.example.waterapp.reportModel.ReportViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyReportBinding
    private var myOrderAdapter: ReportAdapter? = null
    private val reportViewModel: ReportViewModel by viewModels()
    private var reportList: List<ReportResponse.AllReport> = ArrayList()
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        progressDialog = CustomProgressDialog(this)
        activity = this

        Toast.makeText(this, "user id here : $userId", Toast.LENGTH_SHORT).show()

        reportListApi(userId)
        reportListObserver()

        binding.arrowBack.setOnClickListener { finish() }
    }

    private fun reportListObserver() {
        reportViewModel.progressIndicator.observe(this, androidx.lifecycle.Observer {
            // Show progress if needed
        })

        reportViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success
            val message = it.peekContent().message
            reportList = it.peekContent().allReports!!

            if (reportList.isNotEmpty()) {

                binding.reportRecyclerView.isVerticalScrollBarEnabled = true
                binding.reportRecyclerView.isVerticalFadingEdgeEnabled = true
                binding.reportRecyclerView.layoutManager = GridLayoutManager(this, 1)
                myOrderAdapter = ReportAdapter(this, reportList)

                // Set the adapter to RecyclerView
                binding.reportRecyclerView.adapter = myOrderAdapter


            } else {

            }
        }

        reportViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@MyReportActivity, it)
        }
    }
    private fun reportListApi(userId: String) {
        reportViewModel.reportList(userId, activity, progressDialog)
    }
}