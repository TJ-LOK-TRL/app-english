package com.masterproject.englishapp.notification.dailyreminder

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.masterproject.englishapp.notification.AppNotificationManager

class DailyReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        AppNotificationManager.sendNotification(
            applicationContext,
            AppNotificationManager.CHANNEL_REMINDERS,
            2002,
            "Hora de praticar!",
            "Faltam apenas 15 minutos para manteres a tua meta diária.",
            command = "app://open/home"
        )
        return Result.success()
    }
}