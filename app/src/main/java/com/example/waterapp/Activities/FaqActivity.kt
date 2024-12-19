package com.example.waterapp.Activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.FaqModel.FaqResponse
import com.example.waterapp.FaqModel.FaqViewModel
import com.example.waterapp.databinding.ActivityFqaBinding
import com.example.waterapp.adapter.FaqAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FaqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFqaBinding
    private lateinit var activity: Activity
    private lateinit var progressDialog: CustomProgressDialog
    private var myOrderAdapter: FaqAdapter? = null
    private var serviceList: List<FaqResponse.Faq> = ArrayList()
    private val faqViewModel: FaqViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFqaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

        progressDialog = CustomProgressDialog(this)
        activity = this

        getFaqListApi()

        getFaqListObserver()

    }

    private fun getFaqListObserver() {
        faqViewModel.progressIndicator.observe(this, androidx.lifecycle.Observer {
            // Show progress if needed
        })

        faqViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success
            val message = it.peekContent().message
            serviceList = it.peekContent().faq!!

            if (serviceList.isNotEmpty()) {

                binding.faqRecyclerView.isVerticalScrollBarEnabled = true
                binding.faqRecyclerView.isVerticalFadingEdgeEnabled = true
                binding.faqRecyclerView.layoutManager = GridLayoutManager(this, 1)
                myOrderAdapter = FaqAdapter(this, serviceList)

                // Set the adapter to RecyclerView
                binding.faqRecyclerView.adapter = myOrderAdapter


            } else {

            }
        }

        faqViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@FaqActivity, it)
        }
    }


    private fun getFaqListApi() {
        faqViewModel.setFaq(activity, progressDialog)
    }
}
