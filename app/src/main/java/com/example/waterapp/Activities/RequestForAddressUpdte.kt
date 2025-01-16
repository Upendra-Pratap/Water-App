package com.example.waterapp.Activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.adapter.MyRequestForAddressUpdate
import com.example.waterapp.addressUpdateModel.MyAllRequestForAddressUpdateResponse
import com.example.waterapp.addressUpdateModel.MyAllRequestForAddressUpdateViewModel
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityRequestForAddressUpdteBinding
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestForAddressUpdte : AppCompatActivity() {
    private lateinit var binding: ActivityRequestForAddressUpdteBinding
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var sharedPreferences: SharedPreferences
    private var userId = ""
    private val myAllRequestForAddressUpdateViewModel: MyAllRequestForAddressUpdateViewModel by viewModels()
    private  var myRequestForAddressUpdate: MyRequestForAddressUpdate? =null
    private  var myRequestList: List<MyAllRequestForAddressUpdateResponse.Data> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestForAddressUpdteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        binding.backArrow.setOnClickListener { finish() }

        //Observer
        myRequestForAddressUpdate(userId)
        myRequestForAddressUpdateObserver()

    }

    @SuppressLint("SuspiciousIndentation")
    private fun myRequestForAddressUpdateObserver() {
        myAllRequestForAddressUpdateViewModel.progressIndicator.observe(this){

        }
        myAllRequestForAddressUpdateViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success

            if (status == true) {
                myRequestList = listOf(it.peekContent().Data())
                if (myRequestList.isNotEmpty()) {
                    binding.addressUpdateRecyclerView.isVerticalFadingEdgeEnabled = true
                    binding.addressUpdateRecyclerView.isVerticalScrollBarEnabled = true
                    binding.addressUpdateRecyclerView.layoutManager = GridLayoutManager(this, 1)
                    myRequestForAddressUpdate = MyRequestForAddressUpdate(this, myRequestList)

                    binding.addressUpdateRecyclerView.adapter = myRequestForAddressUpdate

                } else {

                }
            }
        }
        myAllRequestForAddressUpdateViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@RequestForAddressUpdte, it)

        }
    }
    private fun myRequestForAddressUpdate(userId: String) {
        myAllRequestForAddressUpdateViewModel.myUpdateAddress(userId,this, progressDialog)
    }
}