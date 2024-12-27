package com.example.waterapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waterapp.R
import com.example.waterapp.databinding.FragmentElectricityBinding

class ElectricityFragment : Fragment() {
    private lateinit var binding: FragmentElectricityBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentElectricityBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.historyText.setOnClickListener {
            binding.historyText.setBackgroundResource(R.drawable.debit_background)
            binding.historyText.setTextColor(resources.getColor(R.color.white))
            binding.crediText.setBackgroundResource(R.drawable.creditback)
            binding.crediText.setTextColor(resources.getColor(R.color.green))
        }

        binding.crediText.setOnClickListener {
            binding.crediText.setBackgroundResource(R.drawable.debit_background)
            binding.crediText.setTextColor(resources.getColor(R.color.white))
            binding.historyText.setBackgroundResource(R.drawable.creditback)
            binding.historyText.setTextColor(resources.getColor(R.color.green))
        }

        return view
    }
}