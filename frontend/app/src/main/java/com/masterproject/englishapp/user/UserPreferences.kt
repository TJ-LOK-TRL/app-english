package com.masterproject.englishapp.user

import com.masterproject.englishapp.data.Language

data class UserPreferences(
    val learningLanguage: Language,
    val feedbackLanguage: Language,
    val notificationsEnabled: Boolean,
    val dailyGoalMinutes: Int
)