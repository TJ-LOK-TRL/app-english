package com.masterproject.englishapp.lessons.content.meaninglesson

sealed class MeaningLessonUiState {
    object Loading : MeaningLessonUiState()
    data class Success(val title: String, val data: MeaningLessonData) : MeaningLessonUiState()
    data class Error(val message: String) : MeaningLessonUiState()
}