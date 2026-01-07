package com.masterproject.englishapp.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.data.user.mapper.toDomain
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.navigation.deeplink.CommandResult
import com.masterproject.englishapp.navigation.deeplink.DeepLinkAction
import com.masterproject.englishapp.navigation.deeplink.DeepLinkParser
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.UserPreferencesStore
import com.masterproject.englishapp.utils.UrlBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userContext: UserContext,
    private val userRepository: UserRepository,
    val userPreferencesStore: UserPreferencesStore,
    val uiEventService: UiEventService
) : ViewModel() {
    var isInitializing by mutableStateOf(true)
        private set

    var lunchedRoute by mutableStateOf<String?>(null)
        private set

    val isLoggedIn: Boolean
        get() = userContext.isLoggedIn()

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

    fun handleDeepLink(command: String?) {
        when (val result = DeepLinkParser.parseCommand(command)) {
            is CommandResult.Action -> {
                DeepLinkAction.fromRouteDispatcher(result.action)?.let {
                    lunchedRoute = UrlBuilder.buildUrl(
                        basePath = result.action,
                        params = result.params
                    )
                }
            }
            is CommandResult.Navigate -> {
                lunchedRoute = result.route
            }
            else -> {}
        }
    }

    fun clearLunchedRoute() {
        lunchedRoute = null
    }
}