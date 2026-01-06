package com.masterproject.englishapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.masterproject.englishapp.MainActivity

object AppNotificationManager {
    // Channels here for now
    const val CHANNEL_LOCATION = "location_alerts"
    const val CHANNEL_REMINDERS = "daily_reminders"

    /**
     * Generic function to fire notifications
     */
    fun sendNotification(
        context: Context,
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        targetScreen: String? = null
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel if not exist yet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = if (channelId == CHANNEL_LOCATION) "Location Context" else "Study Reminders"
            val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // Configure the click
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            targetScreen?.let { putExtra("target_screen", it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            context, notificationId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}