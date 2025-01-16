package com.example.waterapp.sectionFragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.FaqModel.FaqResponse
import com.example.waterapp.FaqModel.FaqViewModel
import com.example.waterapp.adapter.FaqAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.FragmentFaqBinding
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FaqFragment : Fragment() {
    private lateinit var binding: FragmentFaqBinding
    private lateinit var activity: Activity
    private val progressDialog by lazy { CustomProgressDialog(activity) }
    private var myOrderAdapter: FaqAdapter? = null
    private var serviceList: List<FaqResponse.Faq> = ArrayList()
    private val faqViewModel: FaqViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqBinding.inflate(inflater, container, false)

        activity = requireActivity()

        //observer and api
        getFaqListApi()
        getFaqListObserver()

        return binding.root
    }
    private fun getFaqListObserver() {
        faqViewModel.progressIndicator.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            // Show progress if needed
        })

        faqViewModel.mRejectResponse.observe(viewLifecycleOwner) {
            val status = it.peekContent().success

            if (status == true){
                serviceList = it.peekContent().faq!!
                if (serviceList.isNotEmpty()) {
                    binding.faqRecyclerView.isVerticalScrollBarEnabled = true
                    binding.faqRecyclerView.isVerticalFadingEdgeEnabled = true
                    binding.faqRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
                    myOrderAdapter = FaqAdapter(requireContext(), serviceList)

                    binding.faqRecyclerView.adapter = myOrderAdapter

                } else {
                    binding.faqRecyclerView.adapter = myOrderAdapter

                }
            }
        }
        faqViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun getFaqListApi() {
        faqViewModel.setFaq(activity, progressDialog)
    }
}