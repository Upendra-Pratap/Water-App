package com.example.waterapp.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.example.waterapp.Activities.AccountHistoryActivity
import com.example.waterapp.R
import com.example.waterapp.databinding.FragmentAccountBinding
import com.example.waterapp.Activities.AnnouncementActivity
import com.example.waterapp.Activities.ChangePasswordActivity
import com.example.waterapp.Activities.CheckBalanceActivity
import com.example.waterapp.Activities.FaqActivity
import com.example.waterapp.Activities.GenerateReportActivity
import com.example.waterapp.Activities.LoginActivity
import com.example.waterapp.Activities.MyReportActivity
import com.example.waterapp.Activities.NotificationActivity
import com.example.waterapp.Activities.ProfileActivity

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.nextApp.setOnClickListener {
            val intent = Intent(requireActivity(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.notificationCons.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.shareApp.setOnClickListener {
            val intent = Intent(requireActivity(), GenerateReportActivity::class.java)
            startActivity(intent)
        }

        binding.faqApp.setOnClickListener {
            openLogoutPopup()
        }

        binding.myenquiryCons.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.changePasswordApp.setOnClickListener {
            val intent = Intent(requireActivity(), AnnouncementActivity::class.java)
            startActivity(intent)
        }

        binding.faqSectionApp.setOnClickListener {
            val intent = Intent(requireActivity(), FaqActivity::class.java)
            startActivity(intent)
        }

        binding.nextApp1.setOnClickListener {
            val intent = Intent(requireActivity(), AccountHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.logoutApp.setOnClickListener {
            val intent = Intent(requireActivity(), CheckBalanceActivity::class.java)
            startActivity(intent)
        }

        binding.reportApp.setOnClickListener{
            val intent = Intent(requireActivity(), MyReportActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun openLogoutPopup() {
        val builder = AlertDialog.Builder(context, R.style.Style_Dialog_Rounded_Corner)
        val dialogView = layoutInflater.inflate(R.layout.logout_popup, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cancelLogBtn = dialogView.findViewById<AppCompatButton>(R.id.noBtnConst)
        val yesLogoutBtn = dialogView.findViewById<AppCompatButton>(R.id.yesBtnConst)
        dialog.show()

        cancelLogBtn.setOnClickListener {
            dialog.dismiss()
        }
        yesLogoutBtn.setOnClickListener {
            //Call Api
            // logoutApi()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}