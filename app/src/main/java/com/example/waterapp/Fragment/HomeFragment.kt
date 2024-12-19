package com.example.waterapp.Fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waterapp.FaqModel.FaqResponse
import com.example.waterapp.adapter.FaqAdapter
import com.example.waterapp.adapter.ServiceAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.FragmentHomeBinding
import com.example.waterapp.serviceModel.ServiceResponse
import com.example.waterapp.serviceModel.ServicesViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val servicesViewModel: ServicesViewModel by viewModels()
    private var myOrderAdapter: ServiceAdapter? = null
    private var serviceList: List<ServiceResponse.AllService> = ArrayList()
    private lateinit var activity: Activity
    private lateinit var progressDialog: CustomProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        progressDialog = CustomProgressDialog(requireActivity())
        activity = requireActivity()

        getServiceList()
        getServiceObserver()

        return view
    }

    private fun getServiceObserver() {
        servicesViewModel.progressIndicator.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                // Show progress if needed
            })

        servicesViewModel.mRejectResponse.observe(viewLifecycleOwner) {
            val status = it.peekContent().success
            val message = it.peekContent().message
            serviceList = it.peekContent().allServices!!

            if (serviceList.isNotEmpty()) {

                binding.recyclerView.isVerticalScrollBarEnabled = true
                binding.recyclerView.isVerticalFadingEdgeEnabled = true
                binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 1)
                myOrderAdapter = ServiceAdapter(requireContext(), serviceList)

                // Set the adapter to RecyclerView
                binding.recyclerView.adapter = myOrderAdapter


            } else {

            }
        }

        servicesViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }
    private fun getServiceList() {
        servicesViewModel.setService(activity, progressDialog)
    }
}