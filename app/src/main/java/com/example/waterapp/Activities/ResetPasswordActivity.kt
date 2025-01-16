package com.example.waterapp.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.R
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityResetPasswordBinding
import com.example.waterapp.resetPasswordModel.ResetPasswordBody
import com.example.waterapp.resetPasswordModel.ResetPasswordViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()
    private var userId =""
    private lateinit var sharedPreferences: SharedPreferences
    private val progressDialog by lazy { CustomProgressDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        resetPasswordObserver()

        binding.arrowBack.setOnClickListener { finish() }
        binding.submitButton.setOnClickListener { formVelidation() }
    }

    private fun resetPasswordObserver() {
        resetPasswordViewModel.progressIndicator.observe(this){}
        resetPasswordViewModel.mRejectResponse.observe(this){
            val status = it.peekContent().success
            val message = it.peekContent().message
            if (status == true){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        resetPasswordViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@ResetPasswordActivity, it)
        }
    }

    private fun formVelidation() {
        val newPassword = binding.oldPassword.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()

        if (VelidationInputs(newPassword, confirmPassword)) {
            //calling the api of reset password
            resetPasswordApi(newPassword, confirmPassword, userId)

        }
    }
    private fun resetPasswordApi(newPassword: String, confirmPassword: String, userId: String) {
        val resetPasswordBody = ResetPasswordBody(
            newPassword= newPassword,
            confirmPassword = confirmPassword,
        )
        resetPasswordViewModel.resetPassword(resetPasswordBody, userId, this, progressDialog)
    }
    private fun VelidationInputs(newPassword: String, confirmPassword: String): Boolean {
        return when{
            newPassword.isEmpty()->{
                binding.oldPassword.error = getString(R.string.error_user_new_password)
                false
            }
            confirmPassword.isEmpty()->{
                binding.confirmPassword.error = getString(R.string.error_user_confirm_password)
                false
            }
            else -> true
        }
    }
}