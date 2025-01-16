package com.example.waterapp.Fragment

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.adapter.ElectricityBillAdapter
import com.example.waterapp.billWaterElectricity.BillElectricityResponse
import com.example.waterapp.billWaterElectricity.BillElectricityViewModel
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.FragmentElectricityBinding
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ElectricityFragment : Fragment() {
    private lateinit var binding: FragmentElectricityBinding
    private val billElectricityViewModel: BillElectricityViewModel by viewModels()
    private var electricityBillAdapter: ElectricityBillAdapter? = null
    private var electricityBillList: List<BillElectricityResponse.Bill> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private val progressDialog by lazy { CustomProgressDialog(activity) }
    private lateinit var activity: Activity
    private var serviceType = "Electricity"
    private var userId =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentElectricityBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        activity = requireActivity()

        electricityBillApi(userId, serviceType)
        electricityBillObserver()
        return binding.root
    }

    private fun electricityBillObserver() {
        billElectricityViewModel.progressIndicator.observe(viewLifecycleOwner){

        }
        billElectricityViewModel.mRejectResponse.observe(viewLifecycleOwner){
            val status = it.peekContent().success

            if (status == true){
                electricityBillList = it.peekContent().bill!!
                if (electricityBillList.isNotEmpty()){
                    binding.electricityRecyclerView.isVerticalScrollBarEnabled = true
                    binding.electricityRecyclerView.isVerticalFadingEdgeEnabled = true
                    binding.electricityRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
                    electricityBillAdapter = ElectricityBillAdapter(requireContext(), electricityBillList)
                    binding.electricityRecyclerView.adapter = electricityBillAdapter
                }else{

                }
            }
        }

        billElectricityViewModel.errorResponse.observe(viewLifecycleOwner){
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }
    private fun electricityBillApi(userId: String, serviceType: String) {
        billElectricityViewModel.electricityBill(userId, serviceType, activity, progressDialog)
    }
}