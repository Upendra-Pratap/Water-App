package com.example.waterapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.BuildConfig
import com.example.waterapp.R
import com.example.waterapp.billWaterElectricity.BillElectricityResponse

class ElectricityBillAdapter(
    private val context: Context,
    private val electricityBillList: List<BillElectricityResponse.Bill>
): RecyclerView.Adapter<ElectricityBillAdapter.BillViewHolder>() {
    class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.servicesText)
        val totalAmount: TextView = itemView.findViewById(R.id.servicesPrice)
        val startDate: TextView = itemView.findViewById(R.id.ServPrice)
        val endDate: TextView = itemView.findViewById(R.id.custStatus)
        val status: TextView = itemView.findViewById(R.id.billElecStatus)
        val downloadInvoice: Button = itemView.findViewById(R.id.downloadInvoice)
        val consumedUnit: TextView = itemView.findViewById(R.id.servicesNo)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ElectricityBillAdapter.BillViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.electricity_bill, parent, false)
        return ElectricityBillAdapter.BillViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ElectricityBillAdapter.BillViewHolder, position: Int) {
        val myServiceList = electricityBillList[position]
        holder.serviceName.text = electricityBillList[position].serviceType
        holder.totalAmount.text = electricityBillList[position].totalAmount.toString()
        holder.consumedUnit.text = electricityBillList[position].unitConsumed.toString()
        holder.status.text = electricityBillList[position].billStatus.toString()
        holder.startDate.text = electricityBillList[position].billPeriod!!.startDate.toString()
        holder.endDate.text = electricityBillList[position].billPeriod!!.endDate.toString()


        holder.downloadInvoice.setOnClickListener { openBillView(myServiceList) }

    }

    private fun openBillView(myServiceList: BillElectricityResponse.Bill) {
        val billPdfUrl = BuildConfig.IMAGE_KEY + myServiceList.bill
        if (billPdfUrl.isNotEmpty()) {
            openPdf(billPdfUrl)
        } else {
            Toast.makeText(context, "Bill PDF is not available", Toast.LENGTH_SHORT).show()
        }
    }
    private fun openPdf(pdfUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    override fun getItemCount(): Int {
       return electricityBillList.size
    }
}
