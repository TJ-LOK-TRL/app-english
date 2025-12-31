package com.masterproject.englishapp.screens.exercises.speakphrase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.loader.PhraseLoader
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.exercises.SpeakPhrase
import com.masterproject.englishapp.exercises.SpeakPhraseData
import com.masterproject.englishapp.exercises.base.ExerciseInfo
import com.masterproject.englishapp.exercises.base.ExerciseViewModel
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
class SpeakPhraseViewModel @Inject constructor(
    private val phraseLoader: PhraseLoader,
    private val recorder: AudioRecorder,
    private val userContext: UserContext,
    private val uiEventService: UiEventService
) : ViewModel(), ExerciseViewModel<SpeakPhraseData> {

    override var uiState by mutableStateOf<SpeakPhraseData?>(null)
        private set

    override fun loadNext(info: ExerciseInfo) {
        uiState = null
        viewModelScope.launch {
            val manager = SpeakPhrase(phraseLoader)
            val model = userContext.modelOrBlind()

            val selector = AdaptiveSelector<Phrase>(model)

            manager.getData(
                learningLanguage = userContext.learningLanguage,
                feedbackLanguage = userContext.feedbackLanguage,
                phraseSelector = selector,
                recorder = recorder
            )
                .onError { uiEventService.showError(it) }
                .onSuccess { uiState = it }
        }
    }
}