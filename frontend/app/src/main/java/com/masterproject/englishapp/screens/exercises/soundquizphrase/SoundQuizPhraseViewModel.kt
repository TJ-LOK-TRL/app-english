package com.masterproject.englishapp.screens.exercises.soundquizphrase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.Category
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.data.loader.PhraseLoader
import com.masterproject.englishapp.data.loader.TokenLoader
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.data.token.AnyToken
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.exercises.SoundQuiz
import com.masterproject.englishapp.exercises.SoundQuizData
import com.masterproject.englishapp.exercises.SoundQuizPhrase
import com.masterproject.englishapp.exercises.SoundQuizPhraseData
import com.masterproject.englishapp.exercises.base.ExerciseInfo
import com.masterproject.englishapp.exercises.base.ExerciseViewModel
import com.masterproject.englishapp.grammar.GClass
import com.masterproject.englishapp.learning.selector.AdaptiveSelector
import com.masterproject.englishapp.network.ApiService
import com.masterproject.englishapp.result.onError
import com.masterproject.englishapp.result.onSuccess
import com.masterproject.englishapp.selector.RandomSelector
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.modelOrBlind
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SoundQuizPhraseViewModel @Inject constructor(
    private val phraseLoader: PhraseLoader,
    private val api: ApiService,
    private val userContext: UserContext,
    private val uiEventService: UiEventService
) : ViewModel(), ExerciseViewModel<SoundQuizPhraseData> {

    override var uiState by mutableStateOf<SoundQuizPhraseData?>(null)
        private set

    override fun loadNext(info: ExerciseInfo) {
        uiState = null
        viewModelScope.launch {
            val manager = SoundQuizPhrase(phraseLoader, api)
            val model = userContext.modelOrBlind()

            val selector = AdaptiveSelector<Phrase>(model)

            manager.getData(
                learningLanguage = userContext.learningLanguage,
                selector
            )
                .onError { uiEventService.showError(it) }
                .onSuccess { uiState = it }
        }
    }
}