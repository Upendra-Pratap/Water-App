package com.example.waterapp.transactionfragment

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.adapter.HistoryAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.FragmentHistoryBinding
import com.example.waterapp.transactionHistory.TransactionHistoryResponse
import com.example.waterapp.transactionHistory.TransactionHistoryViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private var myOrderAdapter: HistoryAdapter? = null
    private val transactionHistoryViewModel: TransactionHistoryViewModel by viewModels()
    private var transactionHistoryList: List<TransactionHistoryResponse.Datum> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private val progressDialog by lazy { CustomProgressDialog( activity ) }
    private lateinit var activity: Activity
    private var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        activity = requireActivity()

        sharedPreferences = requireContext().getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        getTransactionHistory(userId)
        getTransactionHistoryObserver()

        return binding.root
    }

    private fun getTransactionHistoryObserver() {
        transactionHistoryViewModel.progressIndicator.observe(viewLifecycleOwner){

        }
        transactionHistoryViewModel.mRejectResponse.observe(viewLifecycleOwner) {
            val status = it.peekContent().success
            val message = it.peekContent().message

            if (status == true) {
                transactionHistoryList = it.peekContent().data!!
                if (transactionHistoryList.isNotEmpty()) {
                    binding.historyRecyclerView.isVerticalScrollBarEnabled = true
                    binding.historyRecyclerView.isVerticalFadingEdgeEnabled = true
                    binding.historyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
                    myOrderAdapter = HistoryAdapter(requireContext(), transactionHistoryList)
                    binding.historyRecyclerView.adapter = myOrderAdapter
                } else {
                    binding.historyRecyclerView.adapter = myOrderAdapter
                }
            }
        }
        transactionHistoryViewModel.errorResponse.observe(viewLifecycleOwner){
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }
    private fun getTransactionHistory(userId: String) {
        transactionHistoryViewModel.transactionHistory(
            userId,
            activity
        )
    }
}