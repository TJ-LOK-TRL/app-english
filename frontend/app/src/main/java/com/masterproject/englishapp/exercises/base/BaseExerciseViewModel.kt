package com.masterproject.englishapp.exercises.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.result.onError
import com.masterproject.englishapp.result.onSuccess

abstract class BaseExerciseViewModel<T> : ViewModel() {
    var uiState by mutableStateOf<T?>(null)
        protected set

    var hasError by mutableStateOf(false)
        protected set

    abstract fun loadNext(info: ExerciseInfo)

    protected fun handleResult(result: AppResult<T>, uiEventService: UiEventService) {
        result
            .onError {
                uiEventService.showError(it)
                hasError = true
            }
            .onSuccess {
                uiState = it
                hasError = false
            }
    }
}