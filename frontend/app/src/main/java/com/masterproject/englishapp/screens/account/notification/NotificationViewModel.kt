package com.masterproject.englishapp.screens.account.notification

import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.settings.BaseUserModifierViewModel
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.UserPreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    userContext: UserContext,
    userRepository: UserRepository,
    val preferencesStore: UserPreferencesStore,
    val uiEventService: UiEventService
) : BaseUserModifierViewModel(userContext, userRepository, uiEventService) {

}