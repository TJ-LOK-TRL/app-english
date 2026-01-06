package com.masterproject.englishapp.notification.dailyreminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.masterproject.englishapp.utils.calculateDelayUntil
import java.util.concurrent.TimeUnit

object DailyReminderManager {
    const val WORK_NAME = "daily_study_reminder_work"
    const val TAG = "daily_reminder_tag"
    const val TARGET_HOUR = 7

    fun schedule(context: Context) {
        val delay = calculateDelayUntil(TARGET_HOUR)

        val reminderRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            reminderRequest
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}