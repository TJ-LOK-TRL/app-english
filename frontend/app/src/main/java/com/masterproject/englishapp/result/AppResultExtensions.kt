package com.masterproject.englishapp.result

inline fun <T> AppResult<T>.getOrReturn(
    onError: (AppError) -> Nothing
): T = when (this) {
    is AppResult.Error -> onError(error)
    is AppResult.Success<T> -> data
}

inline fun <T> AppResult<T>.onError(action: (String) -> Unit): AppResult<T> = apply {
    if (this is AppResult.Error) action(this.error.message)
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> = apply {
    if (this is AppResult.Success) action(this.data)
}