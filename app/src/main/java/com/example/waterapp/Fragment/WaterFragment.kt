package com.example.waterapp.Fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.waterapp.R
import com.example.waterapp.databinding.FragmentWaterBinding


class WaterFragment : Fragment() {
    private lateinit var binding: FragmentWaterBinding
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterBinding.inflate(inflater, container, false)

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


        return binding.root
    }
}