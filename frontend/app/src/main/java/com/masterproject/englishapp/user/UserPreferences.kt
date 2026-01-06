package com.masterproject.englishapp.user

import com.masterproject.englishapp.grammar.Language

data class UserPreferences(
    val learningLanguage: Language,
    val feedbackLanguage: Language,
    val notificationsEnabled: Boolean,
    val dailyGoalMinutes: Int,
    val soundEffectsEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val dailyRemindersEnabled: Boolean = true,
    val gpsNotificationsEnabled: Boolean = false
)