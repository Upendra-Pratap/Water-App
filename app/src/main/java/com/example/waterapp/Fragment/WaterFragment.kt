package com.example.waterapp.Fragment

import android.annotation.SuppressLint
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
import com.example.waterapp.databinding.FragmentWaterBinding
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class WaterFragment : Fragment() {
    private lateinit var binding: FragmentWaterBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var activity: Activity
    private val progressDialog by lazy { CustomProgressDialog(activity) }
    private var electricityBillAdapter: ElectricityBillAdapter? = null
    private var electricityBillList: List<BillElectricityResponse.Bill> = ArrayList()
    private var userId = ""
    private val serviceType ="Water"
    private val billElectricityViewModel: BillElectricityViewModel by viewModels()
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("PREFERENCE_NAME",
            AppCompatActivity.MODE_PRIVATE
        )
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        activity = requireActivity()

        //api calling here
        waterBillApi(userId, serviceType)
        waterBillObserver()

        return binding.root
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun waterBillObserver() {
        billElectricityViewModel.progressIndicator.observe(viewLifecycleOwner){

        }
        billElectricityViewModel.mRejectResponse.observe(viewLifecycleOwner){
            val status = it.peekContent().success
            val message = it.peekContent().message

            if (status == true){
                electricityBillList = it.peekContent().bill!!
                if (electricityBillList.isNotEmpty()){
                    binding.waterRecyclerView.isVerticalScrollBarEnabled = true
                    binding.waterRecyclerView.isVerticalFadingEdgeEnabled = true
                    binding.waterRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
                    electricityBillAdapter = ElectricityBillAdapter(requireContext(), electricityBillList)
                    binding.waterRecyclerView.adapter = electricityBillAdapter
                }else{
                    binding.waterRecyclerView.adapter = electricityBillAdapter
                }
            }
        }
        billElectricityViewModel.errorResponse.observe(viewLifecycleOwner){
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun waterBillApi(userId: String, serviceType: String) {
        billElectricityViewModel.electricityBill(userId, serviceType, activity, progressDialog)
    }
}