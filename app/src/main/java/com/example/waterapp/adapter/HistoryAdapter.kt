package com.example.waterapp.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ViewUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R
import com.example.waterapp.transactionHistory.TransactionHistoryResponse
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
    private val context: Context,
    private val transactionHistoryList: List<TransactionHistoryResponse.Datum>
): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
        ): HistoryViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.history_adapter, parent, false)
            return HistoryAdapter.HistoryViewHolder(itemView)
        }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        val transactionStatus = transactionHistoryList[position].transactionStatus.toString()
        holder.userTransaction.text = transactionHistoryList[position].serviceType.toString()
        holder.purchaseBy.text = transactionHistoryList[position].paymentMethod.toString()
        holder.transactionTextView.text = transactionHistoryList[position].id.toString()
        holder.dollerTextView.text = transactionHistoryList[position].amount.toString()

        val serviceType = transactionHistoryList[position].serviceType

        if (serviceType == "water") {
            val billId = transactionHistoryList[position].billId
            val sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("billId", billId)
            editor.apply()
        }else{
            val billId = transactionHistoryList[position].billId
            val sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("billId", billId)
            editor.apply()
        }

        val dateString = transactionHistoryList[position].transactionDate.toString()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(dateString)

        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        holder.dayTextView.text = formattedDate

        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        holder.timeTextView.text = formattedTime

        if (transactionStatus == "successful"){
            holder.confirmedTextView.visibility = View.VISIBLE
            holder.failedTextView.visibility = View.GONE
            holder.confirmedTextView.text = transactionStatus
        }else{
            holder.confirmedTextView.visibility = View.GONE
            holder.failedTextView.visibility = View.VISIBLE
            holder.failedTextView.text = transactionStatus
        }
    }
    override fun getItemCount(): Int {
        return transactionHistoryList.size
    }
    class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userTransaction: TextView = itemView.findViewById(R.id.nameTextView)
        val purchaseBy: TextView = itemView.findViewById(R.id.dateTextView)
        val transactionTextView: TextView = itemView.findViewById(R.id.transactionTextView)
        val dollerTextView: TextView = itemView.findViewById(R.id.dollerTextView)
        val confirmedTextView: TextView = itemView.findViewById(R.id.confirmedTextView)
        val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val failedTextView: TextView = itemView.findViewById(R.id.failedTextView)

    }
}