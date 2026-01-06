package com.masterproject.englishapp.screens.account

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.masterproject.englishapp.components.headers.AccountHeader
import com.masterproject.englishapp.screens.account.legal.PrivacyPolicyScreen
import com.masterproject.englishapp.screens.account.legal.TermsOfServiceScreen
import com.masterproject.englishapp.screens.account.notification.NotificationScreen
import com.masterproject.englishapp.screens.account.preferences.PreferencesScreen
import com.masterproject.englishapp.screens.account.profile.ProfileScreen
import com.masterproject.englishapp.screens.account.security.SecurityScreen
import com.masterproject.englishapp.screens.account.settings.SettingsScreen

@Composable
fun AccountFlowManager(
    onExitFlow: () -> Unit
) {
    var currentPage by remember { mutableStateOf(AccountPage.MAIN_SETTINGS) }

    BackHandler(enabled = currentPage != AccountPage.MAIN_SETTINGS) {
        currentPage = AccountPage.MAIN_SETTINGS
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AccountHeader(
            title = currentPage.title,
            onBackClick = {
                if (currentPage == AccountPage.MAIN_SETTINGS) {
                    onExitFlow()
                } else {
                    currentPage = AccountPage.MAIN_SETTINGS
                }
            }
        )

        Crossfade(
            targetState = currentPage,
            label = "AccountPageTransition"
        ) { page ->
            when (page) {
                AccountPage.MAIN_SETTINGS -> SettingsScreen(
                    onNavigate = { targetPage -> currentPage = targetPage },
                    goBack = { onExitFlow() }
                )
                AccountPage.EDIT_PROFILE -> ProfileScreen()
                AccountPage.NOTIFICATIONS -> NotificationScreen()
                AccountPage.PREFERENCES -> PreferencesScreen()
                AccountPage.ACCOUNT_SECURITY -> SecurityScreen(
                    onAccountDeleted = { onExitFlow() }
                )
                AccountPage.TERMS_OF_SERVICE -> TermsOfServiceScreen()
                AccountPage.PRIVACY_POLICY -> PrivacyPolicyScreen()
                AccountPage.LINKED_ACCOUNTS -> TODO()
            }
        }
    }
}