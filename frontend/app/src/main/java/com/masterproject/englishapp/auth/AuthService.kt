package com.masterproject.englishapp.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

interface AuthService {
    suspend fun login(email: String, password: String): String
    suspend fun signUp(email: String, password: String): String
    fun logout()
    fun getCurrentUserId(): String?
}