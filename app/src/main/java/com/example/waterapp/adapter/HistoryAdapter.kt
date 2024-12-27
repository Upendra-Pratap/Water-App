package com.example.waterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R

class HistoryAdapter(
    private val context: Context
): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
        ): HistoryViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.history_adapter, parent, false)
            return HistoryAdapter.HistoryViewHolder(itemView)

        }
    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {

    }
    override fun getItemCount(): Int {
        return 5
    }
    class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userTransaction: TextView = itemView.findViewById(R.id.nameTextView)
        val purchaseBy: TextView = itemView.findViewById(R.id.dateTextView)
        val transactionTextView: TextView = itemView.findViewById(R.id.transactionTextView)
        val dollerTextView: TextView = itemView.findViewById(R.id.dollerTextView)
        val confirmedTextView: TextView = itemView.findViewById(R.id.confirmedTextView)
        val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)


    }
}