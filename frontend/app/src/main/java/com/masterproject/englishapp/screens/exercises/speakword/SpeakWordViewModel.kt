package com.masterproject.englishapp.screens.exercises.speakword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.loader.TokenLoader
import com.masterproject.englishapp.data.token.AnyToken
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.exercises.SpeakTokenData
import com.masterproject.englishapp.exercises.SpeakWord
import com.masterproject.englishapp.exercises.base.ExerciseInfo
import com.masterproject.englishapp.exercises.base.ExerciseViewModel
import com.masterproject.englishapp.grammar.GClass
import com.masterproject.englishapp.learning.selector.AdaptiveSelector
import com.masterproject.englishapp.recorder.AudioRecorder
import com.masterproject.englishapp.result.onError
import com.masterproject.englishapp.result.onSuccess
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.modelOrBlind
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpeakWordViewModel @Inject constructor(
    private val tokenLoader: TokenLoader,
    private val recorder: AudioRecorder,
    private val userContext: UserContext,
    private val uiEventService: UiEventService
) : ViewModel(), ExerciseViewModel<SpeakTokenData> {

    override var uiState by mutableStateOf<SpeakTokenData?>(null)
        private set

    override fun loadNext(info: ExerciseInfo) {
        uiState = null
        viewModelScope.launch {
            val manager = SpeakWord(tokenLoader)
            val model = userContext.modelOrBlind()

            // Adaptive selector based on user progress
            val selector = AdaptiveSelector<AnyToken>(model)

            manager.getData(
                grammarClasses = setOf(GClass.NOUN),
                categories = setOf(info.category),
                learningLanguage = userContext.learningLanguage,
                feedbackLanguage = userContext.feedbackLanguage,
                tokenSelector = selector,
                recorder = recorder
            )
                .onError { uiEventService.showError(it) }
                .onSuccess { uiState = it }
        }
    }
}