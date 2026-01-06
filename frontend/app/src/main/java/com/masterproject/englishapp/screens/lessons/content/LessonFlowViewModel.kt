package com.masterproject.englishapp.screens.lessons.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.providers.ProviderSource
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.lessons.content.meaninglesson.providers.MeaningLessonAIProvider
import com.masterproject.englishapp.lessons.content.model.LessonConfig
import com.masterproject.englishapp.lessons.content.model.LessonType
import com.masterproject.englishapp.lessons.content.model.LessonRepository
import com.masterproject.englishapp.lessons.content.model.LessonStep
import com.masterproject.englishapp.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonFlowViewModel @Inject constructor(
    private val apiService: ApiService,
    private val uiEventService: UiEventService,
    savedStateHandle: SavedStateHandle,
    lessonRepository: LessonRepository
) : ViewModel() {

    val sessionId: String = checkNotNull(savedStateHandle["sessionId"]) {
        "Session ID is required for LessonFlowViewModel"
    }

    val lessonConfigs: StateFlow<List<LessonConfig>> =
        lessonRepository.getLessonConfigs(sessionId)

    private val _steps = MutableStateFlow<List<LessonStep>>(emptyList())
    val steps = _steps.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            // Wait for the configs and load the content
            lessonConfigs.collect { configs ->
                if (configs.isNotEmpty() && _steps.value.isEmpty()) {
                    loadContent(configs)
                }
            }
        }
    }

    private suspend fun loadContent(configs: List<LessonConfig>) {
        _isLoading.value = true
        try {
            val allSteps = mutableListOf<LessonStep>()

            configs.forEach { config ->
                val provider = when (config.type) {
                    LessonType.MEANING -> when (config.sources.random()) {
                        ProviderSource.GEMINI -> MeaningLessonAIProvider(apiService)
                    }
                }

                allSteps.addAll(provider.getContent(config))
            }
            _steps.value = allSteps
        } catch (e: Exception) {
            uiEventService.showError("Failed to load lesson: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }
}