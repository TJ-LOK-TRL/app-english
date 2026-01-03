package com.masterproject.englishapp.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.masterproject.englishapp.data.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesStore @Inject constructor() {
    var learningLanguage by mutableStateOf<Language?>(null)
    var feedbackLanguage by mutableStateOf<Language?>(null)
    var notificationsEnabled by mutableStateOf(true)
    var dailyGoalMinutes by mutableIntStateOf(15)

    fun toDataModel(): UserPreferences {
        return UserPreferences(
            learningLanguage = learningLanguage!!,
            feedbackLanguage = feedbackLanguage!!,
            notificationsEnabled = notificationsEnabled,
            dailyGoalMinutes = dailyGoalMinutes
        )
    }

    fun loadFromDomain(prefs: UserPreferences) {
        learningLanguage = prefs.learningLanguage
        feedbackLanguage = prefs.feedbackLanguage
        notificationsEnabled = prefs.notificationsEnabled
        dailyGoalMinutes = prefs.dailyGoalMinutes
    }
    
    fun resetToDefaults() {
        learningLanguage = null
        feedbackLanguage = null
        notificationsEnabled = true
        dailyGoalMinutes = 15
    }

}