package com.masterproject.englishapp.screens.account.settings

import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.settings.BaseUserModifierViewModel
import com.masterproject.englishapp.user.UserContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    userContext: UserContext,
    userRepository: UserRepository,
    uiEventService: UiEventService
) : BaseUserModifierViewModel(userContext, userRepository, uiEventService) {
    fun performLogout() {
        userContext.logout()
    }
}