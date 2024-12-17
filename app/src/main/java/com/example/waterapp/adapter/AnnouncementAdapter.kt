package com.example.waterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R

class AnnouncementAdapter(
    private val context: Context
) : RecyclerView.Adapter<AnnouncementAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.announcement_adapter, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.nameTextView.text.toString().trim()
        holder.timeTextView.text.toString().trim()
        holder.messageTextView.text.toString().trim()
    }

    override fun getItemCount(): Int {
        return 8
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.titleMsg)
        val timeTextView: TextView = itemView.findViewById(R.id.descriptionMsg)
        val messageTextView: TextView = itemView.findViewById(R.id.dateText)
    }
}