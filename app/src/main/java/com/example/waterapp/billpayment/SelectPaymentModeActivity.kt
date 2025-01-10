package com.example.waterapp.billpayment

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.waterapp.Dashboard.DashboardActivity
import com.example.waterapp.billpayment.model.BillPaymentBody
import com.example.waterapp.billpayment.model.BillPaymentViewModel
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivitySelectPaymentModeBinding
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class SelectPaymentModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectPaymentModeBinding
    private val billPaymentViewModel: BillPaymentViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialog: CustomProgressDialog
    private var billId: String? = null
    private val electricityBill = "6776280d99b3051d79fa7149"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPaymentModeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressDialog = CustomProgressDialog(this)
        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        billId = sharedPreferences.getString("billId", null)

        val serviceType = intent.getStringExtra("serviceType")

        binding.leftArrow.setOnClickListener {finish()}

        if (serviceType == "electricity") {
            binding.cardOne.setOnClickListener {
                billPaymentApi(electricityBill, "credit card")
            }
            binding.cardTwo.setOnClickListener {
                billPaymentApi(electricityBill, "debit card")
            }
            binding.cardThree.setOnClickListener {
                billPaymentApi(electricityBill, "online payment")
            }
        }else{
            binding.cardOne.setOnClickListener  {
                billId?.let {
                    billPaymentApi(it, "credit card")
                } ?: run {
                    Toast.makeText(this, "Bill ID not found!", Toast.LENGTH_SHORT).show()
                }
            }
            binding.cardTwo.setOnClickListener  {
                billId?.let {
                    billPaymentApi(it, "debit card")
                } ?: run {
                    Toast.makeText(this, "Bill ID not found!", Toast.LENGTH_SHORT).show()
                }
            }
            binding.cardThree.setOnClickListener{
                billId?.let {
                    billPaymentApi(it, "online payment")
                } ?: run {
                    Toast.makeText(this, "Bill ID not found!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        billPaymentObserver()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun billPaymentObserver() {
        billPaymentViewModel.progressIndicator.observe(this) {
        }
        billPaymentViewModel.mRejectResponse.observe(this) {
            val success = it.peekContent().success
            val message = it.peekContent().message

            if (success == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SelectPaymentModeActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Payment failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
        billPaymentViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@SelectPaymentModeActivity, it)
        }
    }
    private fun billPaymentApi(billId: String, paymentMethod: String) {
        val billPaymentBody = BillPaymentBody(
            transactionStatus = "successful",
            paymentMethod = paymentMethod
        )
        billPaymentViewModel.billPayment(billId, billPaymentBody, this, progressDialog)
    }
}

