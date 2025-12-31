package com.masterproject.englishapp.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiEventService @Inject constructor() {
    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 10
    )

    val events = _events.asSharedFlow()

    fun showError(message: String) {
        _events.tryEmit(
            UiEvent.Error(message)
        )
    }

    fun showInfo(message: String) {
        _events.tryEmit(
            UiEvent.Info(message)
        )
    }

    fun showSuccess(message: String) {
        _events.tryEmit(
            UiEvent.Success(message)
        )
    }

    fun showWarning(message: String) {
        _events.tryEmit(
            UiEvent.Warning(message)
        )
    }
}