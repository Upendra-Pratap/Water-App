package com.example.waterapp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.databinding.ActivityForgotBinding
import com.example.waterapp.forgotPasswordModel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotBinding
    private  val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

        binding.forgotbutton.setOnClickListener {
            formVelidation()
        }
    }

    private fun VelidationInputs(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.emailtext.error = "Please enter your email"
                false
            }
            else -> true
        }
    }

    private fun formVelidation() {
        val email = binding.emailtext.text.toString().trim()

        if (VelidationInputs(email)){
            //calling api here
            forgotPasswordApi(email)

        }
    }

    private fun forgotPasswordApi(email: String) {
        val intent = Intent(this@ForgotActivity, OtpVerificationActivity::class.java)
        startActivity(intent)
    }

}