package com.masterproject.englishapp.screens.exercises.boolmeaning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.Category
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.data.loader.PhraseLoader
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.exercises.BoolMeaning
import com.masterproject.englishapp.exercises.BoolMeaningData
import com.masterproject.englishapp.exercises.SoundQuizData
import com.masterproject.englishapp.exercises.base.ExerciseInfo
import com.masterproject.englishapp.exercises.base.ExerciseViewModel
import com.masterproject.englishapp.learning.selector.AdaptiveSelector
import com.masterproject.englishapp.result.onError
import com.masterproject.englishapp.result.onSuccess
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.modelOrBlind
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoolMeaningViewModel @Inject constructor(
    private val phraseLoader: PhraseLoader,
    private val userContext: UserContext,
    private val uiEventService: UiEventService
) : ViewModel(), ExerciseViewModel<BoolMeaningData> {

    override var uiState by mutableStateOf<BoolMeaningData?>(null)
        private set

    override fun loadNext(info: ExerciseInfo) {
        uiState = null
        viewModelScope.launch {
            val manager = BoolMeaning(phraseLoader)
            val model = userContext.modelOrBlind()

            val selector = AdaptiveSelector<Phrase>(model)

            manager.getData(
                learningLanguage = userContext.learningLanguage,
                feedbackLanguage = userContext.feedbackLanguage,
                phraseSelector = selector
            )
                .onError { uiEventService.showError(it) }
                .onSuccess { uiState = it }
        }
    }
}