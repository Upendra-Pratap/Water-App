package com.example.waterapp.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.waterapp.Dashboard.DashboardActivity
import com.example.waterapp.R
import com.example.waterapp.application.WaterApp
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityLoginBinding
import com.example.waterapp.loginModel.LoginBody
import com.example.waterapp.loginModel.LoginViewModel
import com.example.waterapp.utils.ErrorUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var isPasswordVisible = true
    private lateinit var sharedPreferences: SharedPreferences
    private var token = ""
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "1") ?: "1"

        setAppLanguage(savedLanguage)

        loginObserver()

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                Log.e("test_sam_token", "retrieve token successful : $token")
            } else {
                Log.e("test_sam_token", "token should not be null...")
            }
        }.addOnFailureListener { }.addOnCanceledListener {}
            .addOnCompleteListener { task: Task<String> ->
                Log.e("test_sam_token", "This is the token : " + task.result)
                token = task.result
                WaterApp.encryptedPrefs.FCMToken = token
                Log.e("test_sam_token", "   accessToken : $token")
                Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
            }

        getNotificationPermission()

        if (savedLanguage == "1") {
            binding.englishLang.isChecked = true
            binding.frenchLang.isChecked = false
        } else if (savedLanguage == "2") {
            binding.frenchLang.isChecked = true
            binding.englishLang.isChecked = false
        }

        binding.englishLang.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPreferences.edit().putString("selected_language", "1").apply()
                setAppLanguage("1")
                restartApp()
            }
        }

        binding.frenchLang.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPreferences.edit().putString("selected_language", "2").apply()
                setAppLanguage("2")
                restartApp()
            }
        }

        binding.signUpBtnCustomer.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtnCus.setOnClickListener { formValidation() }

        binding.moveNextForgot.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(intent)
        }
        binding.passwordHide.setOnClickListener { passwordShow() }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this@LoginActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@LoginActivity,
                arrayOf<String>(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }
    }

    private fun restartApp() {
        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun setAppLanguage(languageCode: String) {
        val locale = when (languageCode) {
            "1" -> Locale("en")
            "2" -> Locale("fr")
            else -> Locale("en")
        }

        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun loginObserver() {
        loginViewModel.progressIndicator.observe(this) { }

        loginViewModel.mRejectResponse.observe(this) { response ->
            val status = response.peekContent().success
            val message = response.peekContent().message
            val userDetail = response.peekContent().userDetails

            if (status == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(intent)
                userDetail?.let {
                    val userId = it.id.toString()
                    val adminId = it.adminId.toString()

                    sharedPreferences.edit().apply {
                        putString("userId", userId)
                        putString("adminId", adminId)
                        apply()
                    }
                    finish()
                }
            }
        }
        loginViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@LoginActivity, it)
        }
    }

    private fun validationInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.mobileName.error = getString(R.string.error_user_email)
                false
            }

            password.isEmpty() -> {
                binding.passwordText.error = getString(R.string.error_user_password)
                false
            }

            else -> true
        }
    }

    private fun formValidation() {
        val email = binding.mobileName.text.toString().trim()
        val password = binding.passwordText.text.toString().trim()

        if (validationInputs(email, password)) {
            // Call API to login
            loginApi(email, password)
        }
    }

    private fun loginApi(email: String, password: String) {
        val loginBody = LoginBody(
            user_email = email,
            password = password,
            phone_token = token
        )
        loginViewModel.loginAccount(loginBody, this, progressDialog)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun passwordShow() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            binding.passwordText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordHide.setImageResource(R.drawable.passwordshowicon)
        } else {
            binding.passwordText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordText.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.passwordHide.setImageResource(R.drawable.passwordhide)
        }
        binding.passwordText.setSelection(binding.passwordText.text.length)
    }
}
