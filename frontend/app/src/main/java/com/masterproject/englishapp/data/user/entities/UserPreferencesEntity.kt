package com.masterproject.englishapp.data.user.entities

data class UserPreferencesEntity(
    val learningLanguage: String = "EN",
    val feedbackLanguage: String = "PT",
    val notificationsEnabled: Boolean = true,
    val dailyGoalMinutes: Int = 15,
    val soundEffectsEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val dailyRemindersEnabled: Boolean = true,
    val gpsNotificationsEnabled: Boolean = false
)