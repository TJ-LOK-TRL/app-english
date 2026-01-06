package com.masterproject.englishapp.result

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error<T>(val error: AppError, val data: T? = null) : AppResult<T>()
}