package com.example.waterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.waterapp.FaqModel.FaqResponse
import com.example.waterapp.R

class FaqAdapter(
    private val context: Context,
    private val faqList: List<FaqResponse.Faq>
) : RecyclerView.Adapter<FaqAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.faq_adapter, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val myFaqList = faqList[position]
        //holder.nameTextView.text = serviceList[position].serviceName.toString()

        holder.questionSection.text = faqList[position].question.toString()
        holder.answerHere.text = faqList[position].answer.toString()

        holder.downArrow.setOnClickListener {
            holder.faqCardViewAnswer.visibility = View.VISIBLE
        }
        holder.upArrow.setOnClickListener {
            holder.faqCardViewAnswer.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionSection: TextView = itemView.findViewById(R.id.questionSection)
        val downArrow: ImageView = itemView.findViewById(R.id.downArrow)
        val answerHere: TextView = itemView.findViewById(R.id.answerHere)
        val upArrow: ImageView = itemView.findViewById(R.id.upArrow)
        val faqCardViewAnswer: CardView = itemView.findViewById(R.id.faqCardViewAnswer)
    }
}