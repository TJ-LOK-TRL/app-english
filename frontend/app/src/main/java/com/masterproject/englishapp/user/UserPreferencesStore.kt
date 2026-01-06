package com.masterproject.englishapp.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.masterproject.englishapp.grammar.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesStore @Inject constructor() {
    var learningLanguage by mutableStateOf<Language?>(null)
    var feedbackLanguage by mutableStateOf<Language?>(null)
    var notificationsEnabled by mutableStateOf(true)
    var dailyGoalMinutes by mutableIntStateOf(15)
    var soundEffectsEnabled by mutableStateOf(true)
    var vibrationEnabled by mutableStateOf(true)
    var dailyRemindersEnabled by mutableStateOf(true)
    var gpsNotificationsEnabled by mutableStateOf(false)

    fun toDomain(): UserPreferences {
        return UserPreferences(
            learningLanguage = learningLanguage!!,
            feedbackLanguage = feedbackLanguage!!,
            notificationsEnabled = notificationsEnabled,
            dailyGoalMinutes = dailyGoalMinutes,
            soundEffectsEnabled = soundEffectsEnabled,
            vibrationEnabled = vibrationEnabled,
            dailyRemindersEnabled = dailyRemindersEnabled,
            gpsNotificationsEnabled = gpsNotificationsEnabled
        )
    }

    fun loadFromDomain(prefs: UserPreferences) {
        learningLanguage = prefs.learningLanguage
        feedbackLanguage = prefs.feedbackLanguage
        notificationsEnabled = prefs.notificationsEnabled
        dailyGoalMinutes = prefs.dailyGoalMinutes
        soundEffectsEnabled = prefs.soundEffectsEnabled
        vibrationEnabled = prefs.vibrationEnabled
        dailyRemindersEnabled = prefs.dailyRemindersEnabled
        gpsNotificationsEnabled = prefs.gpsNotificationsEnabled
    }

    fun resetToDefaults() {
        learningLanguage = null
        feedbackLanguage = null
        notificationsEnabled = true
        dailyGoalMinutes = 15
        soundEffectsEnabled = true
        vibrationEnabled = true
        dailyRemindersEnabled = true
        gpsNotificationsEnabled = false
    }
}