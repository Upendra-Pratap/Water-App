package com.example.waterapp.Fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.waterapp.Activities.CheckBalanceActivity
import com.example.waterapp.BuildConfig
import com.example.waterapp.R
import com.example.waterapp.adapter.ServiceAdapter
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.FragmentHomeBinding
import com.example.waterapp.serviceModel.ServiceResponse
import com.example.waterapp.serviceModel.ServicesViewModel
import com.example.waterapp.updateProfileModel.GetUpdateProfileViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val servicesViewModel: ServicesViewModel by viewModels()
    private val getUpdateProfileViewModel: GetUpdateProfileViewModel by viewModels()
    private var myOrderAdapter: ServiceAdapter? = null
    private var serviceList: List<ServiceResponse.AllService> = ArrayList()
    private lateinit var activity: Activity
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var sharedPreferences: SharedPreferences
    private var userId =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPreferences = requireContext().getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        progressDialog = CustomProgressDialog(requireActivity())
        activity = requireActivity()

        binding.profileLayout.setOnClickListener { menuPopup() }

        //services observer
        getServiceList()
        getServiceObserver()

        //get profile observer
        getUpdateProfileApi(userId)
        getUpdateProfileObserver()

        return view
    }

    private fun getUpdateProfileApi(userId: String) {
        getUpdateProfileViewModel.getUpdateProfile(userId, progressDialog,activity)
    }

    private fun getUpdateProfileObserver() {
        getUpdateProfileViewModel.progressIndicator.observe(viewLifecycleOwner){

        }
        getUpdateProfileViewModel.mCustomerResponse.observe(viewLifecycleOwner){
            val status = it.peekContent().success
            val userData = it.peekContent().data

            if (status == true){
                if (userData?.userName.isNullOrBlank()) {

                } else {
                    if (userData != null) {
                        if (userData?.userName.isNullOrBlank()) {

                            Toast.makeText(requireActivity(), "User name is empty or null", Toast.LENGTH_SHORT).show()

                        } else {
                            if (userData.userName?.length!! > 3) {

                                val result = userData.userName!!.split("".toRegex()).take(4).joinToString("") + "..."

                                binding.nameTextView.text = Editable.Factory.getInstance().newEditable(result)
                            }
                        }
                    }
                }

                if (userData?.profileImage == null){

                }else{
                    val url = it.peekContent().data?.profileImage
                    Glide.with(this).load(BuildConfig.IMAGE_KEY + url).into(binding.userProfile)

                }
            }
        }
        getUpdateProfileViewModel.errorResponse.observe(viewLifecycleOwner){
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun menuPopup() {
        val popupMenu = PopupMenu(requireContext(), binding.profileLayout)

        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.dashboard_popup, popupMenu.menu)

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.balanceOfElectricityAndWater -> {
                    //call here api of clear data
                    //clearDataApi()
                    true
                }
                R.id.recentTransaction -> {
                    //call here api of clear data
                    val intent = Intent(requireActivity(), CheckBalanceActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.topUp -> {
                    //call here api
                    //clearDataApi()
                    true
                }
                R.id.billPayment -> {
                    //call here api here
                    //clearDataApi()
                    true
                }
                else -> false
            }
        }
    }

    private fun getServiceObserver() {
        servicesViewModel.progressIndicator.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                // Show progress if needed
            })

        servicesViewModel.mRejectResponse.observe(viewLifecycleOwner) {
            val status = it.peekContent().success
            val message = it.peekContent().message
            serviceList = it.peekContent().allServices!!

            if (serviceList.isNotEmpty()) {

                binding.recyclerView.isVerticalScrollBarEnabled = true
                binding.recyclerView.isVerticalFadingEdgeEnabled = true
                binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                myOrderAdapter = ServiceAdapter(requireContext(), serviceList)

                // Set the adapter to RecyclerView
                binding.recyclerView.adapter = myOrderAdapter

            } else {

            }
        }

        servicesViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }
    private fun getServiceList() {
        servicesViewModel.setService(activity, progressDialog)
    }

    override fun onResume() {
        super.onResume()
        getUpdateProfileApi(userId)
    }
}