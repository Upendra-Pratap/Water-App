package com.example.waterapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waterapp.BuildConfig
import com.example.waterapp.R
import com.example.waterapp.reportModel.ReportResponse

class ReportAdapter(
    private val context: Context,
    private val reportList: List<ReportResponse.AllReport>

    ): RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportAdapter.ReportViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.my_report_adapter, parent, false)
        return ReportAdapter.ReportViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ReportAdapter.ReportViewHolder, position: Int) {
        holder.serviceName.text = reportList[position].userName.toString()
        holder.problemType.text = reportList[position].problemType.toString()
        holder.dateOfIncident.text = reportList[position].dateOfIncident.toString()
        holder.address.text = reportList[position].address.toString()
        holder.status.text = reportList[position].status.toString()

        holder.viewImage.setOnClickListener {

            val imageFilename = reportList[position].problemPhoto?.firstOrNull()

            val baseUrl = BuildConfig.IMAGE_KEY
            val imageUrl = baseUrl + imageFilename

            val imageView = ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,

                )
                scaleType = ImageView.ScaleType.FIT_CENTER
            }

            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.electricity)
                .error(R.drawable.water)
                .into(imageView)

            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(imageView)
            dialogBuilder.setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return reportList.size
    }
    class ReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.servicesText)
        val problemType: TextView = itemView.findViewById(R.id.servicesPrice)
        val dateOfIncident: TextView = itemView.findViewById(R.id.servicesNo)
        val address: TextView = itemView.findViewById(R.id.ServPrice)
        val status: TextView = itemView.findViewById(R.id.custStatus)
        val viewImage: Button = itemView.findViewById(R.id.viewImageOfProblem)

    }
}