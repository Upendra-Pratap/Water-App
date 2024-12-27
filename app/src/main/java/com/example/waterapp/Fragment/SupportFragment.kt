package com.example.waterapp.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.waterapp.R
import com.example.waterapp.databinding.FragmentSupportBinding
import com.example.waterapp.sectionFragment.CustomerSupportFragment
import com.example.waterapp.sectionFragment.FaqFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportFragment : Fragment() {
    private lateinit var binding: FragmentSupportBinding
    private var navItemIndex = 0
    private val TAG_NEXT = "next"
    private val TAG_DASH_BOARD = "dashboard"
    private var CURRENT_TAG = TAG_DASH_BOARD
    private val shouldLoadHomeFragOnBackPress = true
    private var doubleBackToExitPressedOnce = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportBinding.inflate(inflater, container, false)
        val view = binding.root

        replaceFragment(FaqFragment())

        binding.faqCons.setOnClickListener {
            replaceFragment(FaqFragment())
            navItemIndex = 1
            CURRENT_TAG = TAG_NEXT
            binding.faq.setTextColor(ContextCompat.getColor(requireActivity(), R.color.green))
            binding.faqView.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.green
                )
            )

            binding.customerSupportView.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.black
                )
            )

            binding.customerSupport.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.black
                )
            )
        }

        binding.customerSupportCons.setOnClickListener {
            replaceFragment(CustomerSupportFragment())
            navItemIndex = 2
            CURRENT_TAG = TAG_NEXT
            binding.faq.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            binding.faqView.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.black
                )
            )
            binding.customerSupportView.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.green
                )
            )
            binding.customerSupport.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.green
                )
            )

        }
        return view
    }

    @Deprecated("Deprecated in Java")
    fun onBackPressed() {
        super.getParentFragment()
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0
                CURRENT_TAG = TAG_DASH_BOARD
                val eventListFragment = FaqFragment()
                replaceFragment(eventListFragment)

            } else {
                if (doubleBackToExitPressedOnce) {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    return
                }

                doubleBackToExitPressedOnce = true
                Toast.makeText(
                    requireActivity(),
                    "Please click BACK again to exit",
                    Toast.LENGTH_SHORT
                ).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 1000)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        binding.faq.setTextColor(ContextCompat.getColor(requireActivity(), R.color.green))
        binding.faqView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.green))
        binding.customerSupportView.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.black
            )
        )
        binding.customerSupport.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.black
            )
        )

        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_Container1, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}