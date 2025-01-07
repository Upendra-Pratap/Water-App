package com.example.waterapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.Activities.SelectPaymentModeActivity
import com.example.waterapp.R
import com.example.waterapp.serviceModel.ServiceResponse

class ServiceAdapter(
    private val context: Context,
    private val serviceList: List<ServiceResponse.AllService>
): RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceViewHolder { val itemView =
        LayoutInflater.from(context).inflate(R.layout.service_adapter, parent, false)
        return ServiceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val myFaqList = serviceList[position]
        holder.serviceName.text = serviceList[position].serviceType.toString()
        holder.productListCons.setOnClickListener {
            val intent = Intent(context, SelectPaymentModeActivity::class.java)
            intent.putExtra("userType", 1)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return serviceList.size
    }
    class ServiceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val serviceName: TextView = itemView.findViewById(R.id.servicesText)
        val productListCons: ConstraintLayout = itemView.findViewById(R.id.productListConst)
    }
}