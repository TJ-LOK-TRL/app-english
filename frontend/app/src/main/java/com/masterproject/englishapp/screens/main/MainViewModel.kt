package com.masterproject.englishapp.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.user.UserRepository
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
            val uid = userContext.getCurrentUid()

            if (uid != null && userContext.currentUser == null) {
                try {
                    val user = userRepository.loadUser(uid)
                    userContext.setUser(user)
                } catch (e: Exception) {
                    userContext.logout()
                }
            }
            isInitializing = false
        }
    }

    val isLoggedIn: Boolean get() = userContext.isLoggedIn()
}