package com.example.waterapp.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivityLoginBinding
import com.example.waterapp.Dashboard.DashboardActivity
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.loginModel.LoginBody
import com.example.waterapp.loginModel.LoginViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var activity: Activity
    private var isPasswordVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressDialog = CustomProgressDialog(this)
        activity = this

        loginObserver()

        binding.signUpBtnCustomer.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtnCus.setOnClickListener {
            formValidation()

        }

        binding.moveNextForgot.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(intent)
        }

        binding.passwordHide.setOnClickListener {
            passwordShow()
        }
    }

    private fun loginObserver() {
        loginViewModel.progressIndicator.observe(this){

        }
        loginViewModel.mRejectResponse.observe(this){
            val status =it.peekContent().success
            val message = it.peekContent().message

            if (status ==  true ){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(intent)

            }

        }
        loginViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@LoginActivity , it)
        }
    }

    private fun validationInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.mobileName.error = "Please enter your email"
                false
            }
            password.isEmpty() -> {
                binding.passwordText.error = "Please enter your password"
                false
            }
            else -> true
        }
    }

    private fun formValidation() {
        val email = binding.mobileName.text.toString().trim()
        val password = binding.passwordText.text.toString().trim()

        if (validationInputs(email, password)){
            //here call the Api
            loginApi(email, password)

        }
    }

    private fun loginApi(email: String, password: String) {
        val loginBody = LoginBody(
            user_email = email,
            password = password
        )
        loginViewModel.loginAccount(loginBody, activity, progressDialog)
    }

    private fun passwordShow() {
         isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible){
                binding.passwordText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordHide.setImageResource(R.drawable.passwordshowicon)
            } else {
                binding.passwordText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordText.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.passwordHide.setImageResource(R.drawable.passwordhide)
            }
            binding.passwordText.setSelection(binding.passwordText.text.length)
        }

}