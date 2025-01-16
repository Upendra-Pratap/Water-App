package com.example.waterapp.Activities

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.adapter.MyRequestAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityGetRequestForSupportBinding
import com.example.waterapp.requestForSupportModel.GetRequestForSupportResponse
import com.example.waterapp.requestForSupportModel.GetRequestForSupportViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetRequestForSupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetRequestForSupportBinding
    private val getRequestForSupportViewModel: GetRequestForSupportViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var myOrderAdapter: MyRequestAdapter? = null
    private var myRequestList: List<GetRequestForSupportResponse.UserRequest> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetRequestForSupportBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        //observer
        getRequestSupportApi(userId)
        requestSupportObserver()

    }
    private fun requestSupportObserver() {
        getRequestForSupportViewModel.progressIndicator.observe(this) {

        }
        getRequestForSupportViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success
            myRequestList = it.peekContent().userRequests!!

            if (status == true) {
                binding.myRequestRecyclerView.isVerticalScrollBarEnabled = true
                binding.myRequestRecyclerView.isVerticalFadingEdgeEnabled = true
                binding.myRequestRecyclerView.layoutManager = GridLayoutManager(this, 1)
                myOrderAdapter = MyRequestAdapter(this, myRequestList)
                binding.myRequestRecyclerView.adapter = myOrderAdapter

            } else {

            }

        }
        getRequestForSupportViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@GetRequestForSupportActivity, it)
        }
    }

    private fun getRequestSupportApi(userId: String) {
        getRequestForSupportViewModel.getRequestForSupport(userId, this, progressDialog)
    }
}