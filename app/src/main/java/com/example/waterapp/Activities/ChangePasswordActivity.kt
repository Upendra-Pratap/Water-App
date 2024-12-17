package com.example.waterapp.Activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private var isPasswordVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

        binding.emailIcon.setOnClickListener {
            passwordShow()
        }

        binding.newpassword.setOnClickListener {
            newPasswordShow()
        }

        binding.loginBtn.setOnClickListener {
            changePasswordVelidation()
        }
    }

    private fun VelidationInputs(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        return when{
            oldPassword.isEmpty()->{
                binding.oldPassword.error = "Enter old password"
                false
            }
            newPassword.isEmpty()->{
                binding.newpass.error = "Enter new password"
                false
            }
            confirmPassword.isEmpty()->{
                binding.confirmPassword.error = "Enter confirm password"
                false
            }
            else-> true
        }
    }

    private fun changePasswordVelidation() {
        val oldPassword = binding.oldPassword.text.toString().trim()
        val newPassword = binding.newpass.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()

        if (VelidationInputs(oldPassword, newPassword, confirmPassword)){
            //calling Api here
            changePasswordApi()

        }
    }

    private fun changePasswordApi() {
        val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun newPasswordShow() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            binding.newpass.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.newpassword.setImageResource(R.drawable.passwordshowicon)
        } else {
            binding.newpass.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.newpass.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.newpassword.setImageResource(R.drawable.passwordhide)
        }
        binding.newpass.setSelection(binding.newpass.text.length)
    }

    private fun passwordShow() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            binding.oldPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.emailIcon.setImageResource(R.drawable.passwordshowicon)
        } else {
            binding.oldPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.oldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.emailIcon.setImageResource(R.drawable.passwordhide)
        }
        binding.oldPassword.setSelection(binding.oldPassword.text.length)
    }

}