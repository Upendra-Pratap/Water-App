package com.example.waterapp.Activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.waterapp.BuildConfig
import com.example.waterapp.Dashboard.DashboardActivity
import com.example.waterapp.addBalanceModel.AddBalanceBody
import com.example.waterapp.addBalanceModel.AddBalanceViewModel
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityAddBalanceBinding
import com.example.waterapp.updateProfileModel.GetUpdateProfileViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBalanceBinding
    private val getUpdateProfileViewModel: GetUpdateProfileViewModel by viewModels()
    private val addBalanceViewModel: AddBalanceViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit  var progressDialog: CustomProgressDialog
    private var userId = ""
    private var userType = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBalanceBinding.inflate(layoutInflater)
        val view = binding.root

        userType = intent.getIntExtra("userType", 0)

        progressDialog = CustomProgressDialog(this)

        sharedPreferences = application.getSharedPreferences("PREFERENCE_NAME",
            MODE_PRIVATE
        )
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        binding.arrowBack.setOnClickListener { finish() }

        binding.addBalanceButton.setOnClickListener {
            formValidation()
        }
        setContentView(view)

        //observer
        getUpdateProfileApi(userId)
        getUpdateProfileObserver()
        addBalanceObserver()

    }
    private fun formValidation() {
        val enterAmount = binding.enterAmount.text.toString().toInt()
        if (addBalanceValidation(enterAmount.toString())){
                addBalanceApi(userId, enterAmount)

        }
    }
    private fun addBalanceValidation(enterAmount: String): Boolean {
        return when {
            enterAmount.isEmpty() -> {
                binding.enterAmount.error = "Please Enter Amount"
                false
            }

            else -> true
        }
    }
    private fun addBalanceObserver() {
        addBalanceViewModel.progressIndicator.observe(this){

        }
        addBalanceViewModel.mRejectResponse.observe(this){
            val status = it.peekContent().success
            val message = it.peekContent().message
            if (status == true){
                val intent = Intent(this@AddBalanceActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()

            }else{

            }
        }
        addBalanceViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@AddBalanceActivity, it)
        }
    }

    private fun addBalanceApi(userId: String, enterAmount: Int) {
        val addBalanceBody = AddBalanceBody(
            topupType = userType,
            balance = enterAmount
        )
        addBalanceViewModel.addBalance(userId, addBalanceBody, this)
    }
    private fun getUpdateProfileObserver() {
        getUpdateProfileViewModel.progressIndicator.observe(this){

        }
        getUpdateProfileViewModel.mCustomerResponse.observe(this){
            val status = it.peekContent().success
            val userData = it.peekContent().data

            if (status == true){
                if (userData?.userName == null){

                }else{
                    binding.userName.text = Editable.Factory.getInstance().newEditable(userData.userName.toString())
                }
                if (userData?.profileImage == null){

                }else{
                    val url = it.peekContent().data?.profileImage
                    Glide.with(this).load(BuildConfig.IMAGE_KEY + url).into(binding.profileImg)

                }
            }
        }
        getUpdateProfileViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@AddBalanceActivity, it)
        }
    }
    private fun getUpdateProfileApi(userId: String) {
        getUpdateProfileViewModel.getUpdateProfile(userId, progressDialog,this)
    }
}


