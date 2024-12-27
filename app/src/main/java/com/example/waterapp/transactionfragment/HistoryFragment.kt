package com.example.waterapp.transactionfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.adapter.HistoryAdapter
import com.example.waterapp.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private var myOrderAdapter: HistoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.historyRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 1)
        myOrderAdapter = HistoryAdapter(requireContext())
        binding.historyRecyclerView.adapter = myOrderAdapter

        return binding.root
    }
}