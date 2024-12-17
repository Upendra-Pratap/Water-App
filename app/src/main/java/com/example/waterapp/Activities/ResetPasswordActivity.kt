package com.example.waterapp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

        binding.submitButton.setOnClickListener {
            formVelidation()
        }
    }
    private fun formVelidation() {
        val newPassword = binding.oldPassword.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()

        if (VelidationInputs(newPassword, confirmPassword)) {
            //calling the api of reset password
            resetPasswordApi()

        }
    }

    private fun resetPasswordApi() {
        val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun VelidationInputs(newPassword: String, confirmPassword: String): Boolean {
        return when{
            newPassword.isEmpty()->{
                binding.oldPassword.error = "Please New Password"
                false
            }
            confirmPassword.isEmpty()->{
                binding.confirmPassword.error = "Please Enter Confirm Password"
                false
            }
            else -> true
        }
    }
}