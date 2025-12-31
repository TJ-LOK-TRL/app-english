package com.masterproject.englishapp.exercises.base

interface ExerciseViewModel<T> {
    val uiState: T?
    fun loadNext(info: ExerciseInfo)
}