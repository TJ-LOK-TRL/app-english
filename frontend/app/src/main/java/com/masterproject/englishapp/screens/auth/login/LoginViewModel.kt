package com.masterproject.englishapp.screens.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.user.UserContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userContext: UserContext
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    fun login(email: String, password: String, onResult: (AppResult<Unit>) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                withTimeout(30000L) {
                    // Auth
                    val uid = userContext.authenticate(email, password)

                    // Load user data
                    val userModel = userRepository.loadUser(uid)

                    // Set actual user in the app context
                    userContext.setUser(userModel)

                    onResult(AppResult.Success(Unit))
                }
            } catch (e: TimeoutCancellationException) {
                onResult(AppResult.Error(AppError.Unknown(Exception("Timeout: Connection is too slow."))))
            } catch (e: Exception) {
                onResult(AppResult.Error(AppError.Unknown(e)))
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        userContext.logout()
    }
}