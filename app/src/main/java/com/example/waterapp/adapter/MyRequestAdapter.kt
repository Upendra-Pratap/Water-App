package com.example.waterapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waterapp.BuildConfig
import com.example.waterapp.R
import com.example.waterapp.requestForSupportModel.GetRequestForSupportResponse

class MyRequestAdapter(  private val context: Context,
    private val myRequestList: List<GetRequestForSupportResponse.UserRequest>
): RecyclerView.Adapter<MyRequestAdapter.MyRequestViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyRequestViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.myrequest_adapter, parent, false)
        return MyRequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyRequestViewHolder, position: Int) {
        val contact = myRequestList[position].contactDetails
        val address = myRequestList[position].address
        holder.userName.text = myRequestList[position].userName.toString()
        holder.serviceName.text = myRequestList[position].serviceName.toString()
        holder.userEmail.text = contact!!.email.toString()
        holder.phoneNumber.text = contact.phoneNumber.toString()
        holder.messages.text = myRequestList[position].issueMessage.toString()
        holder.address.text = address!!.street.toString()
        holder.status.text = myRequestList[position].status.toString()

        holder.buttonOfImageService.setOnClickListener { openPopup(position) }

    }

    private fun openPopup(position: Int) {
        val imageFilename = myRequestList[position].uploadPhotos?.firstOrNull()

        val baseUrl = BuildConfig.IMAGE_KEY

        val imageUrl = baseUrl + imageFilename

        val imageView = ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                )
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.electricity)
            .error(R.drawable.water)
            .into(imageView)

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(imageView)
        dialogBuilder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun getItemCount(): Int {
        return myRequestList.size
    }

    class MyRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.servicesText)
        val serviceName: TextView = itemView.findViewById(R.id.servicesPrice)
        val userEmail: TextView = itemView.findViewById(R.id.emailSupport)
        val phoneNumber: TextView = itemView.findViewById(R.id.phoneNumber)
        val messages: TextView = itemView.findViewById(R.id.servicesNo)
        val address: TextView = itemView.findViewById(R.id.ServPrice)
        val status: TextView = itemView.findViewById(R.id.custStatus)
        val buttonOfImageService: Button = itemView.findViewById(R.id.viewImageOfProblem)

    }
}