package com.example.waterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.R
import com.example.waterapp.notificationModel.NotificationResponse

class NotificationAdapter(
    private val context: Context,
    private val notificationList: List<NotificationResponse.AllNotification>
): RecyclerView.Adapter<NotificationAdapter.MessageViewHolder>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.notification_adapter, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.timeTextView.text = notificationList[position].message.toString()
        holder.messageTextView.text= notificationList[position].date.toString()
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.descriptionMsg)
        val messageTextView: TextView = itemView.findViewById(R.id.dateText)
    }
}