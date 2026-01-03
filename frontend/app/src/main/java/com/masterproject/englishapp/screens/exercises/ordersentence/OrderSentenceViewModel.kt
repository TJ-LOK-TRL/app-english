package com.masterproject.englishapp.screens.exercises.ordersentence

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.loader.PhraseLoader
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.exercises.OrderSentence
import com.masterproject.englishapp.exercises.OrderSentenceData
import com.masterproject.englishapp.exercises.base.ExerciseInfo
import com.masterproject.englishapp.exercises.base.BaseExerciseViewModel
import com.masterproject.englishapp.learning.selector.AdaptiveSelector
import com.masterproject.englishapp.result.onError
import com.masterproject.englishapp.result.onSuccess
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.modelOrBlind
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderSentenceViewModel @Inject constructor(
    private val phraseLoader: PhraseLoader,
    private val userContext: UserContext,
    private val uiEventService: UiEventService
) : BaseExerciseViewModel<OrderSentenceData>() {

    override fun loadNext(info: ExerciseInfo) {
        uiState = null

        viewModelScope.launch {
            val manager = OrderSentence(phraseLoader)

            val result = manager.getData(
                learningLanguage = userContext.learningLanguage,
                feedbackLanguage = userContext.feedbackLanguage,
                phraseSelector = AdaptiveSelector(userContext.modelOrBlind())
            )

            handleResult(result, uiEventService)
        }
    }
}

