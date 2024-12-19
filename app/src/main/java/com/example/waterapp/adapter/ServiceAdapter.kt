package com.example.waterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R
import com.example.waterapp.serviceModel.ServiceResponse

class ServiceAdapter(
    private val context: Context,
    private val serviceList: List<ServiceResponse.AllService>
): RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.service_adapter, parent, false)
        return ServiceViewHolder(itemView)    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val myFaqList = serviceList[position]
        holder.serviceName.text = serviceList[position].serviceType.toString()
        holder.unitPrice.text = serviceList[position].ratePerUnit.toString()

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class ServiceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val serviceName: TextView = itemView.findViewById(R.id.servicesText)
        val unitPrice: TextView = itemView.findViewById(R.id.unitPrice)
        val serviceRequestBtn: AppCompatButton = itemView.findViewById(R.id.addServices)

    }
}