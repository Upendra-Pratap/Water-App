package com.example.waterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R
import com.example.waterapp.addressUpdateModel.MyAllRequestForAddressUpdateResponse

class MyRequestForAddressUpdate(
    private val context: Context,
// Declare it as a list of 'Data' objects
    private val myRequestList: List<MyAllRequestForAddressUpdateResponse.Data> = listOf()
) : RecyclerView.Adapter<MyRequestForAddressUpdate.MyRequestViewHolder>() {

    class MyRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.servicesText)
        val emailId: TextView = itemView.findViewById(R.id.emailSupport)
        val streetAddress: TextView = itemView.findViewById(R.id.phoneNumber)
        val cityAddress: TextView = itemView.findViewById(R.id.servicesNo)
        val pinAddress: TextView = itemView.findViewById(R.id.ServPrice)
        val status: TextView = itemView.findViewById(R.id.custStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRequestViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.my_request_for_address_update, parent, false)
        return MyRequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyRequestViewHolder, position: Int) {
        val data = myRequestList[position]

        holder.userName.text = data.userName ?: "No Name"
        holder.emailId.text = data.userEmail ?: "No Email"

        val address = data.address
        holder.streetAddress.text = address?.street ?: "No Street"
        holder.cityAddress.text = address?.city ?: "No City"
        holder.pinAddress.text = address?.zip?.toString() ?: "No Zip"
        holder.status.text = data.status?.toString() ?: "No Status"
    }

    override fun getItemCount(): Int {
        return myRequestList.size
    }
}
