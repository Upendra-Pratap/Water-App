package com.example.waterapp.Activities

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.waterapp.Fragment.HomeFragment
import com.example.waterapp.R
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityCheckBalanceBinding
import com.example.waterapp.transactionHistory.TransactionHistoryViewModel
import com.example.waterapp.transactionfragment.HistoryFragment
import com.example.waterapp.transactionfragment.TransactionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckBalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckBalanceBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var activity: Activity
    private val transactionHistoryViewModel: TransactionHistoryViewModel by viewModels()
    private lateinit var progressDialog: CustomProgressDialog
    private var navItemIndex = 0
    private val TAG_NEXT = "next"
    private val TAG_DASH_BOARD = "dashboard"
    private var CURRENT_TAG = TAG_DASH_BOARD
    private val shouldLoadHomeFragOnBackPress = true
    private var doubleBackToExitPressedOnce = false
    private var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckBalanceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

        activity = this
        progressDialog = CustomProgressDialog(this)

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()


        replaceFragment(HistoryFragment())

        binding.historyText.setOnClickListener {
            replaceFragment(HistoryFragment())
            navItemIndex = 1
            CURRENT_TAG = TAG_NEXT
            binding.historyText.setBackgroundResource(R.drawable.debit_background)
            binding.historyText.setTextColor(resources.getColor(R.color.white))
            binding.crediText.setBackgroundResource(R.drawable.creditback)
            binding.crediText.setTextColor(resources.getColor(R.color.green))
        }

        binding.crediText.setOnClickListener {
            replaceFragment(TransactionFragment())
            navItemIndex = 2
            CURRENT_TAG = TAG_NEXT
            binding.crediText.setBackgroundResource(R.drawable.debit_background)
            binding.crediText.setTextColor(resources.getColor(R.color.white))
            binding.historyText.setBackgroundResource(R.drawable.creditback)
            binding.historyText.setTextColor(resources.getColor(R.color.green))
        }
    }
        private fun replaceFragment(fragment: Fragment) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_Container1, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0
                CURRENT_TAG = TAG_DASH_BOARD
                binding.historyText.setBackgroundResource(R.drawable.debit_background)
                binding.historyText.setTextColor(resources.getColor(R.color.white))
                binding.crediText.setBackgroundResource(R.drawable.creditback)
                binding.crediText.setTextColor(resources.getColor(R.color.green))
                val eventListFragment = HistoryFragment()
                replaceFragment(eventListFragment)

            } else {
                if (doubleBackToExitPressedOnce) {
                    onBackPressedDispatcher.onBackPressed()
                    return
                }
                doubleBackToExitPressedOnce = true
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 1000)
            }
        }
    }
}