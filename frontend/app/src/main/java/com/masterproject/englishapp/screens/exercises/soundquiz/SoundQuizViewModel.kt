package com.masterproject.englishapp.screens.exercises.soundquiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.loader.TokenLoader
import com.masterproject.englishapp.data.token.AnyToken
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.exercises.SoundQuiz
import com.masterproject.englishapp.exercises.SoundQuizData
import com.masterproject.englishapp.exercises.base.ExerciseInfo
import com.masterproject.englishapp.exercises.base.BaseExerciseViewModel
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
class SoundQuizViewModel @Inject constructor(
    private val tokenLoader: TokenLoader,
    private val api: ApiService,
    private val userContext: UserContext,
    private val uiEventService: UiEventService
) : BaseExerciseViewModel<SoundQuizData>() {

    override fun loadNext(info: ExerciseInfo) {
        uiState = null

        viewModelScope.launch {
            val manager = SoundQuiz(tokenLoader, api)

            val result = manager.getData(
                grammarClasses = setOf(GClass.NOUN),
                categories = setOf(info.category),
                languages = setOf(userContext.learningLanguage),
                rightTokenSelector = AdaptiveSelector(userContext.modelOrBlind()),
                wrongTokensSelector = RandomSelector()
            )

            handleResult(result, uiEventService)
        }
    }
}