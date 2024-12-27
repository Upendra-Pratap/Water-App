package com.example.waterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R


class TransactionAdapter(
    private val context: Context
): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>(){
    class TransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.nameTextView)
        val setDate: TextView = itemView.findViewById(R.id.dateTextView)
        val typeOfProduct: TextView = itemView.findViewById(R.id.anotherTextView)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.transaction_adapter, parent, false)
        return  TransactionViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: TransactionAdapter.TransactionViewHolder, position: Int) {
        holder.userName.text.toString()
        holder.setDate.text.toString()
        holder.typeOfProduct.text.toString()

    }

    override fun getItemCount(): Int {
        return 5
    }
}