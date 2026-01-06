package com.masterproject.englishapp.notification.dailyreminder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.masterproject.englishapp.user.UserPreferencesStore

@Composable
fun DailyReminderHandler(
    userPreferencesStore: UserPreferencesStore
) {
    val context = LocalContext.current
    val dailyReminderEnabled = userPreferencesStore.notificationsEnabled

    LaunchedEffect(dailyReminderEnabled) {
        if (dailyReminderEnabled) {
            DailyReminderManager.schedule(context)
            android.util.Log.d("DailyReminder", "Reminder targeted for ${DailyReminderManager.TARGET_HOUR}")
        } else {
            DailyReminderManager.cancel(context)
            android.util.Log.d("DailyReminder", "Reminder canceled by user")
        }
    }
}