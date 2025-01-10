package com.example.waterapp.Activities


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.waterapp.R
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityOtpVerificationBinding
import com.example.waterapp.otpVerificationModel.OtpVerificationViewModel
import com.example.waterapp.otpVerificationModel.OtpVerifiicationBody
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerificationBinding
    private val otpVerifiicationViewModel: OtpVerificationViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

        binding.verifyBtn.setOnClickListener {
            val otpFields = listOf(
                binding.box5.text.toString(),
                binding.box1.text.toString(),
                binding.box2.text.toString(),
                binding.box3.text.toString(),
                binding.box4.text.toString(),
                binding.box6.text.toString(),
            )
            if (otpFields.any { it.isEmpty() }) {
                Toast.makeText(this, getString(R.string.Please_enter_OTP), Toast.LENGTH_SHORT)
                    .show()
            } else {
                var verificationCode =
                    "${binding.box5.text}${binding.box1.text}${binding.box2.text}${binding.box3.text}${binding.box4.text}${binding.box6.text}"

                //here call the Api
                 otpVerificationApi(verificationCode)

                otpVerficationObserver()
            }
        }

        //below the code of the moving to next box
        binding.box5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.box1.requestFocus()
                }
            }
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.box1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.box2.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.box2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.box3.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.box3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.box4.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.box4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.box6.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.box6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    binding.box6.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Handle focus shift when backspace is pressed
        binding.box5.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.box5.text.isEmpty()) {
                return@setOnKeyListener true
            }
            false
        }

        binding.box1.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.box1.text.isEmpty()) {
                binding.box5.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.box2.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.box2.text.isEmpty()) {
                binding.box1.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.box3.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.box3.text.isEmpty()) {
                binding.box2.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.box4.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.box4.text.isEmpty()) {
                binding.box3.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.box6.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.box6.text.isEmpty()) {
                binding.box4.requestFocus()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun otpVerficationObserver() {
        otpVerifiicationViewModel.progressIndicator.observe(this){

        }
        otpVerifiicationViewModel.mRejectResponse.observe(this){
            val status = it.peekContent().success
            val message = it.peekContent().message

            if (status == true){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@OtpVerificationActivity, ResetPasswordActivity::class.java)
                startActivity(intent)
            }
        }
        otpVerifiicationViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@OtpVerificationActivity, it)
        }

    }

    private fun otpVerificationApi(verificationCode: String) {
        val otpVerifiicationBody = OtpVerifiicationBody(
            otp = verificationCode
        )
        otpVerifiicationViewModel.otpVerificationAccount(otpVerifiicationBody, this, progressDialog)
    }
}