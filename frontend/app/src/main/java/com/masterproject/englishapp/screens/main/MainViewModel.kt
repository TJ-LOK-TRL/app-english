package com.masterproject.englishapp.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.data.user.mapper.toDomain
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.user.UserContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userContext: UserContext,
    private val userRepository: UserRepository,
    val uiEventService: UiEventService
) : ViewModel() {
    var isInitializing by mutableStateOf(true)
        private set

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            try {
                val uid = userContext.getCurrentUid()
                if (uid != null) {
                    val user = userRepository.loadUser(uid).toDomain()
                    userContext.setUser(user)
                }
            } catch (e: Exception) {
                uiEventService.showError("Session error: ${e.localizedMessage}")
                userContext.logout()
            } finally {
                isInitializing = false
            }
        }
    }

    val isLoggedIn: Boolean get() = userContext.isLoggedIn()
}