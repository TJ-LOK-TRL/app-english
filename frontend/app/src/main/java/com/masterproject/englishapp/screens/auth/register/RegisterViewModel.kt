package com.masterproject.englishapp.screens.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.data.user.mapper.toEntity
import com.masterproject.englishapp.learning.bkt.BKTKnowledgeModel
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.UserModel
import com.masterproject.englishapp.user.UserPreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userPreferencesStore: UserPreferencesStore,
    private val userRepository: UserRepository,
    private val userContext: UserContext
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    fun register(name: String, email: String, password: String, onResult: (AppResult<Unit>) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                withTimeout(30000L) {
                    // Create Firebase Auth Account
                    val uid = userContext.signUp(email, password)

                    // Create initial UserModel object
                    val newUser = UserModel(
                        id = uid,
                        name = name,
                        email = email,
                        preferences = userPreferencesStore.toDomain(),
                        model = BKTKnowledgeModel()
                    )

                    // Save in the storage
                    userRepository.createUser(uid, newUser.toEntity())

                    // Set actual user
                    userContext.setUser(newUser)

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
}