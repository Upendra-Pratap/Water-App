package com.example.waterapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waterapp.databinding.FragmentWaterBinding


class WaterFragment : Fragment() {
    private lateinit var binding: FragmentWaterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWaterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}