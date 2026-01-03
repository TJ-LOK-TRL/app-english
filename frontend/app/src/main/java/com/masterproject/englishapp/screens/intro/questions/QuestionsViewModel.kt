package com.masterproject.englishapp.screens.intro.questions

import androidx.lifecycle.ViewModel
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.user.UserPreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    val preferencesStore: UserPreferencesStore
) : ViewModel() {
    fun updateLearningLanguage(lang: Language) {
        preferencesStore.learningLanguage = lang
    }

    fun updateFeedbackLanguage(lang: Language) {
        preferencesStore.feedbackLanguage = lang
    }
}