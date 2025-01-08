package com.example.waterapp.Activities


interface NotificationClickListener {
    fun deleteNotification(position: Int, id: String)
    fun seeNotification(position: Int, id: String)

}
