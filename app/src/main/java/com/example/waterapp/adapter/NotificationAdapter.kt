package com.example.waterapp.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.Activities.NotificationClickListener
import com.example.waterapp.R
import com.example.waterapp.notificationModel.NotificationResponse

class NotificationAdapter(
    private val context: Context,
    private val onItemClick: NotificationClickListener,
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
        val notificationId = notificationList[position].notificationId

        holder.timeTextView.text = notificationList[position].message.toString()
        holder.messageTextView.text= notificationList[position].date.toString()

        holder.deleteNotification.setOnClickListener {
            openDeleteDialog(position, notificationId!!)
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun openDeleteDialog(position: Int, id: String) {
        val builder = AlertDialog.Builder(context, R.style.Style_Dialog_Rounded_Corner)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.delete_notification_popup, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val NoBtnAcceptNDel = dialogView.findViewById<TextView>(R.id.NoBtnAcceptNDel)
        val YesBtnAcceptNdel = dialogView.findViewById<TextView>(R.id.YesBtnAcceptNdel)

        NoBtnAcceptNDel.setOnClickListener {
            dialog.dismiss()
        }
        YesBtnAcceptNdel.setOnClickListener {
            onItemClick.deleteNotification(position, id)
            notifyDataSetChanged()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.descriptionMsg)
        val messageTextView: TextView = itemView.findViewById(R.id.dateText)
        val deleteNotification: ImageView = itemView.findViewById(R.id.deleteNotification)
    }
}