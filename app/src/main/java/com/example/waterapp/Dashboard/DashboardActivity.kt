package com.example.waterapp.Dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.waterapp.R
import com.example.waterapp.databinding.ActivityDashboardBinding
import com.example.waterapp.Fragment.AccountFragment
import com.example.waterapp.Fragment.ElectricityFragment
import com.example.waterapp.Fragment.HomeFragment
import com.example.waterapp.Fragment.SupportFragment
import com.example.waterapp.Fragment.WaterFragment


class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var navItemIndex = 0
    private val TAG_NEXT = "next"
    private val TAG_DASH_BOARD = "dashboard"
    private var CURRENT_TAG = TAG_DASH_BOARD
    private val shouldLoadHomeFragOnBackPress = true
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        replaceFragment(HomeFragment())

        binding.HomeFragmentUser.setOnClickListener {
            replaceFragment(HomeFragment())
            navItemIndex = 1
            CURRENT_TAG = TAG_NEXT
            binding.homeIconUser.setImageResource(R.drawable.homecolo1)
            binding.orderIconUsers.setImageResource(R.drawable.electricpower)
            binding.walletIconUsers.setImageResource(R.drawable.waterdrop)
            binding.profileUser.setImageResource(R.drawable.technicalsupport)
            binding.settingUser.setImageResource(R.drawable.useraccount)
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.green))
            binding.orderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.walletTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.hisTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.settingTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        binding.ordersFragmentUser.setOnClickListener {
            replaceFragment(ElectricityFragment())
            navItemIndex = 2
            CURRENT_TAG = TAG_NEXT
            binding.homeIconUser.setImageResource(R.drawable.smarthome)
            binding.orderIconUsers.setImageResource(R.drawable.electricalservicecolor)
            binding.walletIconUsers.setImageResource(R.drawable.waterdrop)
            binding.profileUser.setImageResource(R.drawable.technicalsupport)
            binding.settingUser.setImageResource(R.drawable.useraccount)
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.orderTextUser.setTextColor(ContextCompat.getColor(this, R.color.green))
            binding.walletTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.hisTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.settingTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        binding.linearWalletUser.setOnClickListener {
            replaceFragment(WaterFragment())
            navItemIndex = 3
            CURRENT_TAG = TAG_NEXT
            binding.homeIconUser.setImageResource(R.drawable.smarthome)
            binding.orderIconUsers.setImageResource(R.drawable.electricpower)
            binding.walletIconUsers.setImageResource(R.drawable.waterdropcolor)
            binding.profileUser.setImageResource(R.drawable.technicalsupport)
            binding.settingUser.setImageResource(R.drawable.useraccount)
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.orderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.walletTextUser.setTextColor(ContextCompat.getColor(this, R.color.green))
            binding.hisTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.settingTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        binding.rideHistoryUserL.setOnClickListener {
            replaceFragment(SupportFragment())
            navItemIndex = 4
            CURRENT_TAG = TAG_NEXT
            binding.homeIconUser.setImageResource(R.drawable.smarthome)
            binding.orderIconUsers.setImageResource(R.drawable.electricpower)
            binding.walletIconUsers.setImageResource(R.drawable.waterdrop)
            binding.profileUser.setImageResource(R.drawable.technicalsupportcolor)
            binding.settingUser.setImageResource(R.drawable.useraccount)
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.orderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.walletTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.hisTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.green))
            binding.settingTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        binding.settingUserL.setOnClickListener {
            replaceFragment(AccountFragment())
            navItemIndex = 5
            CURRENT_TAG = TAG_NEXT
            binding.homeIconUser.setImageResource(R.drawable.smarthome)
            binding.orderIconUsers.setImageResource(R.drawable.electricpower)
            binding.walletIconUsers.setImageResource(R.drawable.waterdrop)
            binding.profileUser.setImageResource(R.drawable.technicalsupport)
            binding.settingUser.setImageResource(R.drawable.useraccountcolor)
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.orderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.walletTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.hisTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.settingTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.green))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0
                CURRENT_TAG = TAG_DASH_BOARD
                val eventListFragment = HomeFragment()
                replaceFragment(eventListFragment)

            } else {
                if (doubleBackToExitPressedOnce) {
                    onBackPressedDispatcher.onBackPressed()
                    return
                }

                doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 1000)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        binding.homeIconUser.setImageResource(R.drawable.homecolo1)
        binding.orderIconUsers.setImageResource(R.drawable.electricpower)
        binding.walletIconUsers.setImageResource(R.drawable.waterdrop)
        binding.profileUser.setImageResource(R.drawable.technicalsupport)
        binding.settingUser.setImageResource(R.drawable.useraccount)
        binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.green))
        binding.orderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.walletTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.hisTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.settingTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_Container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}