package com.example.waterapp.electricityFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waterapp.R
import com.example.waterapp.databinding.FragmentPendingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PendingFragment : Fragment() {
    private lateinit var binding: FragmentPendingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}