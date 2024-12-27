package com.example.waterapp.transactionfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterapp.adapter.TransactionAdapter
import com.example.waterapp.databinding.FragmentTransactionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private var myOrderAdapter: TransactionAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false)


        binding.transactionRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 1)
        myOrderAdapter = TransactionAdapter(requireContext())

        // Set the adapter to RecyclerView
        binding.transactionRecyclerView.adapter = myOrderAdapter

        return binding.root
    }
}