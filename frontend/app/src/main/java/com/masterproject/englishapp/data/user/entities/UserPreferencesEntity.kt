package com.masterproject.englishapp.data.user.entities

data class UserPreferencesEntity(
    val learningLanguage: String = "EN",
    val feedbackLanguage: String = "PT",
    val notificationsEnabled: Boolean = true,
    val dailyGoalMinutes: Int = 15
)