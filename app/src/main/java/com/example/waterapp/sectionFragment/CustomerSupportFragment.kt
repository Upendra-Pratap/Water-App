package com.example.waterapp.sectionFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waterapp.Activities.RequestForSupportActivity
import com.example.waterapp.chat.ChatActivity
import com.example.waterapp.databinding.FragmentCustomerSupportBinding

class CustomerSupportFragment : Fragment() {
    private lateinit var binding: FragmentCustomerSupportBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerSupportBinding.inflate(inflater, container, false)

        binding.customerServices.setOnClickListener {
            val intent = Intent(requireActivity(), ChatActivity::class.java)
            startActivity(intent)
        }



        binding.emailSupport.setOnClickListener {
            openComposeEmail()
        }

        binding.phoneSupport.setOnClickListener {
            openPhoneDialer()
        }

        binding.requestForCustomerSupport.setOnClickListener {
            val intent = Intent(requireActivity(), RequestForSupportActivity::class.java)
            startActivity(intent)

        }


        return binding.root
    }

    // Function to open email client with pre-filled details
    private fun openComposeEmail() {
        val emailUri = Uri.parse("mailto:support@example.com")

        val emailIntent = Intent(Intent.ACTION_SENDTO, emailUri)

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please describe your issue...")

        val packageManager = requireContext().packageManager
        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(emailIntent)
        } else {

        }
    }

    // Function to open phone dialer with pre-filled phone number
    private fun openPhoneDialer() {
        val phoneNumber = "tel:+9076692182"
        val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber))
        val packageManager = requireContext().packageManager
        if (phoneIntent.resolveActivity(packageManager) != null) {
            startActivity(phoneIntent)
        } else {
        }
    }
}