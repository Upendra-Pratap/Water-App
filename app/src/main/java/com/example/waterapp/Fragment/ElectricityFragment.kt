package com.example.waterapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waterapp.databinding.FragmentElectricityBinding

class ElectricityFragment : Fragment() {
    private lateinit var binding: FragmentElectricityBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentElectricityBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}