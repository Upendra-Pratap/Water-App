package com.example.waterapp.chat.chatadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.chatModel.GetDoChatResponse
import com.example.waterapp.databinding.ItemMessageSentBinding

class ChatAdapter(
    private var messageList: List<GetDoChatResponse.Response>
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding =
            ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        val userType = message.isAdmin
        holder.bind(message, userType)
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    inner class MessageViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: GetDoChatResponse.Response, userType: Int?) {
            if (!message.message.isNullOrEmpty()) {
                if (userType == 0) {
                    binding.textMessage.visibility = View.VISIBLE
                    binding.textMessage.text = message.message
                    binding.chatCMsgLeft.visibility = View.GONE

                } else if (userType == 1) {
                    binding.chatCMsgLeft.visibility = View.VISIBLE
                    binding.chatCMsgLeft.text = message.message
                    binding.textMessage.visibility = View.GONE
                }
            } else {
                binding.textMessage.visibility = View.GONE
                binding.chatCMsgLeft.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addChat(list: List<GetDoChatResponse.Response>) {
        messageList = list
        notifyDataSetChanged()
    }
}
