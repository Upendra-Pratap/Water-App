package com.example.waterapp.chat.chatadapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.chatModel.GetDoChatResponse
import com.example.waterapp.chatModel.SingleChatDeleteViewModel
import com.example.waterapp.databinding.ItemMessageSentBinding

class ChatAdapter(
    private val context: Context,
    private val singleChatDeleteViewModel: SingleChatDeleteViewModel,
    private var messageList: List<GetDoChatResponse.Response>
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        val userType = message.isAdmin
        val idMessage = message.id
        holder.bind(message, userType)

        holder.itemView.setOnLongClickListener {
            showDeletePopup(idMessage)
            true
        }
    }

    private fun showDeletePopup(idMessage: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Yes") { dialog, _ ->
                idMessage?.let { deleteMessageFromApi(it) }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteMessageFromApi(idMessage: String) {
        singleChatDeleteViewModel.singleChatDelete(idMessage, object :
            SingleChatDeleteViewModel.DeleteCallback() {
            override fun onDeleteSuccess() {
                val position = messageList.indexOfFirst { it.id == idMessage }
                if (position != -1) {
                    messageList = messageList.toMutableList().apply { removeAt(position) }
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, messageList.size)
                }
            }

            override fun onDeleteError(error: String) {
                // Handle error case (e.g., show a Toast message or a Snackbar)
            }
        })
    }

    override fun getItemCount(): Int = messageList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addChat(list: List<GetDoChatResponse.Response>) {
        messageList = list
        notifyDataSetChanged()
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
}
