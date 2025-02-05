package com.example.waterapp.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.R
import com.example.waterapp.changePasswordModel.ChangePasswordBody
import com.example.waterapp.changePasswordModel.ChangePasswordViewModel
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityChangePasswordBinding
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()
    private var isPasswordVisible = true
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var sharedPreferences: SharedPreferences
    private var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        //observer
        changePasswordObserver()

        binding.arrowBack.setOnClickListener { finish() }

        binding.emailIcon.setOnClickListener {passwordShow()}

        binding.newpassword.setOnClickListener { newPasswordShow() }

        binding.loginBtn.setOnClickListener { changePasswordValidation() }
    }

    private fun changePasswordObserver() {
        changePasswordViewModel.progressIndicator.observe(this){

        }
        changePasswordViewModel.mRejectResponse.observe(this){
            val status = it.peekContent().success
            val message = it.peekContent().message

            if (status == true){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }

        }
        changePasswordViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@ChangePasswordActivity, it)
        }

    }

    private fun ValidationInputs(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        return when{
            oldPassword.isEmpty()->{
                binding.oldPassword.error = getString(R.string.error_user_old_password)
                false
            }
            newPassword.isEmpty()->{
                binding.newpass.error = getString(R.string.error_user_new_password)
                false
            }
            confirmPassword.isEmpty()->{
                binding.confirmPassword.error = getString(R.string.error_user_confirm_password)
                false
            }
            else-> true
        }
    }

    private fun changePasswordValidation() {
        val oldPassword = binding.oldPassword.text.toString().trim()
        val newPassword = binding.newpass.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()

        if (ValidationInputs(oldPassword, newPassword, confirmPassword)){
            //calling Api here
            changePasswordApi(oldPassword, newPassword, confirmPassword,userId)

        }
    }

    private fun changePasswordApi(oldPassword: String, newPassword: String, confirmPassword: String, userId: String) {
        val changePasswordBody = ChangePasswordBody(
            oldPassword = oldPassword,
            newPassword = newPassword,
            confirmNewPassword = confirmPassword,

        )
        changePasswordViewModel.changePassword(userId, changePasswordBody, this, progressDialog)
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