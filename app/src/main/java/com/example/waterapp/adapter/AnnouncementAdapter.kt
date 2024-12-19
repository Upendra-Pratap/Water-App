package com.example.waterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R
import com.example.waterapp.announcementModel.AnnouncementResponse

class AnnouncementAdapter(
    private val context: Context,
    private val announcementList: List<AnnouncementResponse.Datum>
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
        holder.nameTextView.text = announcementList[position].title.toString()
        holder.timeTextView.text = announcementList[position].message.toString()
        holder.messageTextView.text = announcementList[position].anncuncementDate.toString()
    }

    override fun getItemCount(): Int {
        return announcementList.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.titleMsgHere)
        val timeTextView: TextView = itemView.findViewById(R.id.MessageMsg)
        val messageTextView: TextView = itemView.findViewById(R.id.dateText)
    }
}