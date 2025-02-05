package com.example.waterapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.R
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityForgotBinding
import com.example.waterapp.forgotPasswordModel.ForgotPasswordBody
import com.example.waterapp.forgotPasswordModel.ForgotPasswordViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotBinding
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        forgotPasswordObserver()

        binding.arrowBack.setOnClickListener { finish() }
        binding.forgotbutton.setOnClickListener { formValidation() }
    }

    private fun forgotPasswordObserver() {
        forgotPasswordViewModel.progressIndicator.observe(this) {

        }
        forgotPasswordViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success
            val message = it.peekContent().message

            if (status == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ForgotActivity, OtpVerificationActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        forgotPasswordViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@ForgotActivity, it)
        }
    }

    private fun ValidationInputs(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.emailtext.error = getString(R.string.error_user_email)
                false
            }

            else -> true
        }
    }

    private fun formValidation() {
       val email = binding.emailtext.text.toString().trim()

        if (ValidationInputs(email)) {
            //calling api here
            forgotPasswordApi(email)

        }
    }

    private fun forgotPasswordApi(email: String) {
        val forgotPasswordBody = ForgotPasswordBody(
            user_email = email
        )
        forgotPasswordViewModel.forgotPasswordAccount(forgotPasswordBody, this, progressDialog)
    }


}