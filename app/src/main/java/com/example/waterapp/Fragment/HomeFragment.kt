package com.example.waterapp.Fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.Activities.AddBalanceActivity
import com.example.waterapp.adapter.HistoryAdapter
import com.example.waterapp.adapter.ServiceAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.FragmentHomeBinding
import com.example.waterapp.serviceModel.ServiceResponse
import com.example.waterapp.serviceModel.ServicesViewModel
import com.example.waterapp.transactionHistory.TransactionHistoryResponse
import com.example.waterapp.transactionHistory.TransactionHistoryViewModel
import com.example.waterapp.updateProfileModel.GetUpdateProfileViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val servicesViewModel: ServicesViewModel by viewModels()
    private val transactionHistoryViewModel: TransactionHistoryViewModel by viewModels()
    private val getUpdateProfileViewModel: GetUpdateProfileViewModel by viewModels()
    private var myOrderAdapter: ServiceAdapter? = null
    private var serviceList: List<ServiceResponse.AllService> = ArrayList()
    private var transactionAdapter: HistoryAdapter? =null
    private var transactionHistoryList: List<TransactionHistoryResponse.Datum> = ArrayList()
    private lateinit var activity: Activity
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var sharedPreferences: SharedPreferences
    private var userId =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPreferences = requireContext().getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        progressDialog = CustomProgressDialog(requireActivity())
        activity = requireActivity()

        // For the Electricity Account (pass 1)
        binding.withDrawBtn.setOnClickListener {
            val intent = Intent(requireActivity(), AddBalanceActivity::class.java)
            intent.putExtra("userType", 1)
            startActivity(intent)
        }

        // For the Water Account (pass 2)
        binding.withDrawBtnWater.setOnClickListener {
            val intent = Intent(requireActivity(), AddBalanceActivity::class.java)
            intent.putExtra("userType", 2)
            startActivity(intent)
        }

        //services observer
        getServiceList()
        getServiceObserver()

        //observer
        getUpdateProfileApi(userId)
        getUpdateProfileObserver()

        //call the transaction observer
        transactionHistoryApi(userId)
        transactionHistoryObserver()

        return view
    }
    private fun getUpdateProfileObserver() {
        getUpdateProfileViewModel.progressIndicator.observe(viewLifecycleOwner){

        }
        getUpdateProfileViewModel.mCustomerResponse.observe(viewLifecycleOwner){
            val status = it.peekContent().success
            val userData = it.peekContent().data

            if (status == true){
                if (userData?.currentBalanceElectricity == null){

                }else{
                    binding.accountBalanceDri.text = Editable.Factory.getInstance().newEditable("₹" + userData.currentBalanceElectricity.toString())
                }
                if(userData?.currentBalanceWater == null){

                }else{
                    binding.waterAccountBalanceDri.text = Editable.Factory.getInstance().newEditable("₹" + userData.currentBalanceWater.toString())
                }
            }
        }
        getUpdateProfileViewModel.errorResponse.observe(viewLifecycleOwner){
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }
    private fun getUpdateProfileApi(userId: String) {
        getUpdateProfileViewModel.getUpdateProfile(userId, progressDialog, activity)
    }

    private fun transactionHistoryObserver() {
        transactionHistoryViewModel.progressIndicator.observe(viewLifecycleOwner){

        }
        transactionHistoryViewModel.mRejectResponse.observe(viewLifecycleOwner){
            transactionHistoryList = it.peekContent().data!!

            if (transactionHistoryList.isNotEmpty()){

                binding.recyclerViewTransaction.isVerticalScrollBarEnabled = true
                binding.recyclerViewTransaction.isVerticalFadingEdgeEnabled = true
                binding.recyclerViewTransaction.layoutManager = GridLayoutManager(requireContext(), 1)
                transactionAdapter = HistoryAdapter(requireContext(), transactionHistoryList)
                binding.recyclerViewTransaction.adapter = transactionAdapter

            }else{

            }
        }
        transactionHistoryViewModel.errorResponse.observe(viewLifecycleOwner){
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun transactionHistoryApi(userId: String) {
        transactionHistoryViewModel.transactionHistory(userId, activity)

    }

    private fun getServiceObserver() {
        servicesViewModel.progressIndicator.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                // Show progress if needed
            })

        servicesViewModel.mRejectResponse.observe(viewLifecycleOwner) {
            val status = it.peekContent().success
            val message = it.peekContent().message
            serviceList = it.peekContent().allServices!!

            if (serviceList.isNotEmpty()) {

                binding.recyclerView.isVerticalScrollBarEnabled = true
                binding.recyclerView.isVerticalFadingEdgeEnabled = true
                binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
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