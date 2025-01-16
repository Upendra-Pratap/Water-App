package com.example.waterapp.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.waterapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import java.util.Date

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.e("info_status", "onMessageReceived123")
        }

        //Check if message contains a notification payload.
        Log.e("BookActivity", "onMessageReceived123  " + remoteMessage.notification!!.title)
        Log.e("BookActivity", "onMessageReceived123  " + remoteMessage.notification!!.body)
        Log.e("BookActivity", "onMessageReceived12313  " + remoteMessage.data)

        if (remoteMessage.notification != null) {
            sendNotification(
                remoteMessage.notification!!.body,
                remoteMessage.notification!!.title
            )
        }
    }

    private fun sendNotification(
        messageBody: String?,
        title: String?
    ) {
        val pendingIntent: PendingIntent
        val intent1 = Intent("ACTION_GOT_IT")

        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0, intent1, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                REQUEST_CODE,
                intent1,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val channelId = System.currentTimeMillis().toString() + ""
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setContentText(messageBody)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setTimeoutAfter(15000)
        notificationBuilder.setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager != null) {
            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationChannel.enableLights(true)
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            val notificationId =
                (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt()

            notificationManager.notify(
                notificationId,
                notificationBuilder.build()
            )

        }
    }
}