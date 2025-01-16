package com.example.waterapp.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.waterapp.Activities.AccountHistoryActivity
import com.example.waterapp.R
import com.example.waterapp.databinding.FragmentAccountBinding
import com.example.waterapp.Activities.AnnouncementActivity
import com.example.waterapp.Activities.ChangePasswordActivity
import com.example.waterapp.Activities.CheckBalanceActivity
import com.example.waterapp.Activities.GenerateReportActivity
import com.example.waterapp.Activities.GetRequestForSupportActivity
import com.example.waterapp.Activities.LoginActivity
import com.example.waterapp.Activities.MyReportActivity
import com.example.waterapp.Activities.NotificationActivity
import com.example.waterapp.Activities.ProfileActivity
import com.example.waterapp.Activities.RequestForAddressUpdte
import com.example.waterapp.Activities.RequestForSupportActivity
import com.example.waterapp.BuildConfig
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.updateProfileModel.GetUpdateProfileViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var activity: Activity
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private lateinit var sharedPreferences: SharedPreferences
    private val getUpdateProfileViewModel: GetUpdateProfileViewModel by viewModels()
    private var userId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPreferences = requireContext().getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        activity = requireActivity()

        getUpdateProfileApi(userId)
        getUpdateProfileObserver()

        binding.nextApp.setOnClickListener {
            val intent = Intent(requireActivity(), ChangePasswordActivity::class.java)
            activity.startActivity(intent)
        }
        binding.logoutApp.setOnClickListener {
            val intent = Intent(requireActivity(), CheckBalanceActivity::class.java)
            activity.startActivity(intent)

        }
        binding.notificationCons.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            activity.startActivity(intent)
        }
        binding.shareApp.setOnClickListener {
            val intent = Intent(requireActivity(), GenerateReportActivity::class.java)
            activity.startActivity(intent)
        }
        binding.faqApp.setOnClickListener {openLogoutPopup()}

        binding.myenquiryCons.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            activity.startActivity(intent)
        }
        binding.changePasswordApp.setOnClickListener {
            val intent = Intent(requireActivity(), AnnouncementActivity::class.java)
            activity.startActivity(intent)
        }
        binding.nextApp1.setOnClickListener {
            val intent = Intent(requireActivity(), AccountHistoryActivity::class.java)
            activity.startActivity(intent)
        }
        binding.billSectionApp.setOnClickListener {
            val intent = Intent(requireActivity(), RequestForSupportActivity::class.java)
           activity.startActivity(intent)
        }
        binding.myRequestApp.setOnClickListener {
            val intent = Intent(requireActivity(), GetRequestForSupportActivity::class.java)
            activity.startActivity(intent)
        }
        binding.reportApp.setOnClickListener {
            val intent = Intent(requireActivity(), MyReportActivity::class.java)
            activity.startActivity(intent)
        }
        binding.myAddressUpdateRequest.setOnClickListener {
            val intent = Intent(requireActivity(), RequestForAddressUpdte::class.java)
            activity.startActivity(intent)
        }
        return view
    }

    private fun getUpdateProfileObserver() {
        getUpdateProfileViewModel.progressIndicator.observe(viewLifecycleOwner){

        }
        getUpdateProfileViewModel.mCustomerResponse.observe(viewLifecycleOwner){
            val status = it.peekContent().success
            val userData = it.peekContent().data

            if (status == true){
                if (userData?.userName == null){

                }else{
                    binding.nameTextView.text = Editable.Factory.getInstance().newEditable(userData.userName.toString())
                }
                if (userData?.profileImage == null){

                }else{
                    val url = it.peekContent().data?.profileImage
                    Glide.with(this).load(BuildConfig.IMAGE_KEY + url).into(binding.userProfile)
                }
            }
        }
        getUpdateProfileViewModel.errorResponse.observe(viewLifecycleOwner){
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }
    private fun getUpdateProfileApi(userId: String) {
        getUpdateProfileViewModel.getUpdateProfile(userId, progressDialog,activity)
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
           logoutApi()
        }
    }
    private fun logoutApi() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        getUpdateProfileApi(userId)
    }
}