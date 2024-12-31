package com.example.waterapp.electricityFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waterapp.R
import com.example.waterapp.databinding.FragmentConfirmedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmedFragment : Fragment() {
    private lateinit var binding: FragmentConfirmedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}