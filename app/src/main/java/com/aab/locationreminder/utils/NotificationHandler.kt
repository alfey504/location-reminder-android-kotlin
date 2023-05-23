package com.aab.locationreminder.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Message
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.aab.locationreminder.R
import java.util.*

class NotificationHandler(private val context: Context) {

    companion object {
        private const val CHANNEL_NAME = "location_reminder_notification_channel"
        private const val DESCRIPTIONS = "notification to alert you when you are near a location"
        private const val CHANNEL_ID = "location_reminder_notification_channel"
    }

    // register a notification channel
    fun createNotificationChannel() {
        val name = CHANNEL_NAME
        val descriptionText = DESCRIPTIONS
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // display a simple notification
    fun displaySimpleNotification(
        title: String,
        message: String
    ){
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationId = Random().nextInt()
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

}