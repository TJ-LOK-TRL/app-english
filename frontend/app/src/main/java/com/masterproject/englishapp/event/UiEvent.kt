package com.masterproject.englishapp.event

sealed interface UiEvent {
    data class Error(val message: String) : UiEvent
    data class Info(val message: String) : UiEvent
    data class Success(val message: String) : UiEvent
    data class Warning(val message: String) : UiEvent
}
