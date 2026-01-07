package com.masterproject.englishapp.screens.lessons.content.meaninglesson.contextualdispatcher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.providers.ProviderSource
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.lessons.content.model.LessonConfig
import com.masterproject.englishapp.lessons.content.model.LessonRepository
import com.masterproject.englishapp.lessons.content.model.LessonType
import com.masterproject.englishapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeaningLessonContextualViewModel @Inject constructor(
    private val repository: LessonRepository,
    private val uiEventService: UiEventService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val context: String? = savedStateHandle["context"]

    private val _sessionId = MutableStateFlow<String?>(null)
    val sessionId = _sessionId.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    init {
        val currentContext = context
        if (currentContext.isNullOrBlank()) {
            _isError.value = true
            uiEventService.showError("Context is missing")
        } else {
            createAndDispatch(currentContext)
        }
    }

    private fun createAndDispatch(context: String) {
        viewModelScope.launch {
            try {
                val fullRoute = buildRoute(context)
                val id = fullRoute.substringAfterLast("/")
                if (id.isNotEmpty()) {
                    _sessionId.value = id
                } else {
                    _isError.value = true
                }
            } catch (e: Exception) {
                _isError.value = true
                uiEventService.showError("Failed to create session: ${e.message}")
            }
        }
    }

    private fun buildRoute(context: String): String {
        val sessionId = repository.createSession(
            configs = listOf(
                LessonConfig(context, LessonType.MEANING, setOf(ProviderSource.GEMINI))
            )
        )
        return "${Screen.LESSONS.route}/$sessionId"
    }
}