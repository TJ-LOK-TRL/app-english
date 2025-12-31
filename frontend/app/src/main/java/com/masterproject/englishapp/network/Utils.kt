package com.masterproject.englishapp.network

import retrofit2.HttpException
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import java.io.IOException

suspend fun <T> safeApiCall(
    block: suspend () -> T
): AppResult<T> = try {
    AppResult.Success(block())
} catch (e: IOException) {
    AppResult.Error(AppError.Network)
} catch (e: HttpException) {
    AppResult.Error(AppError.Server)
} catch (e: Exception) {
    AppResult.Error(AppError.Unknown(e))
}