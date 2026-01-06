package com.masterproject.englishapp.screens.account.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.cards.Rect3DCard
import com.masterproject.englishapp.components.loaders.LoadingScreen
import com.masterproject.englishapp.components.loaders.OverlayLoader
import com.masterproject.englishapp.screens.account.components.SettingToggleField
import com.masterproject.englishapp.user.UserPreferences
import com.masterproject.englishapp.utils.DummyUserPreferences

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val user by viewModel.userState.collectAsState()
    var isSaving by remember { mutableStateOf(false) }

    if (user != null) {
        var draftPrefs by remember(user) { mutableStateOf(user!!.preferences) }

        Box(modifier = Modifier.fillMaxSize()) {
            NotificationScreenContent(
                preferences = draftPrefs,
                isSaving = isSaving,
                onPrefsChange = { draftPrefs = it },
                onSave = {
                    val updatedUser = user!!.copy(preferences = draftPrefs)
                    viewModel.performUpdate(updatedUser) { /* UiEventService used internally */ }
                }
            )

            if (isSaving) {
                OverlayLoader()
            }
        }
    } else {
        LoadingScreen()
    }
}
@Composable
fun NotificationScreenContent(
    preferences: UserPreferences,
    isSaving: Boolean,
    onPrefsChange: (UserPreferences) -> Unit,
    onSave: () -> Unit
) {
    val isGeneralEnabled = preferences.notificationsEnabled

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Rect3DCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettingToggleField(
                        label = "General Notifications",
                        checked = preferences.notificationsEnabled,
                        onCheckedChange = { isChecked ->
                            onPrefsChange(preferences.copy(notificationsEnabled = isChecked))
                        }
                    )

                    SettingToggleField(
                        label = "Daily Learning Reminders",
                        checked = if (isGeneralEnabled) preferences.dailyRemindersEnabled else false,
                        enabled = isGeneralEnabled,
                        onCheckedChange = {
                            onPrefsChange(preferences.copy(dailyRemindersEnabled = it))
                        }
                    )

                    SettingToggleField(
                        label = "GPS Notification",
                        checked = if (isGeneralEnabled) preferences.gpsNotificationsEnabled else false,
                        enabled = isGeneralEnabled,
                        onCheckedChange = {
                            onPrefsChange(preferences.copy(gpsNotificationsEnabled = it))
                        }
                    )
                }
            }
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Salvar Alterações",
            enabled = !isSaving,
            onClick = onSave
        )
    }
}

@Preview(
    name = "Xiaomi Redmi 9C",
    device = "spec:width=360dp,height=800dp,dpi=269",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEEEEEE
)
@Composable
fun NotificationScreenPreview() {
    var dummyPrefs by remember { mutableStateOf(DummyUserPreferences) }

    NotificationScreenContent(
        preferences = dummyPrefs,
        isSaving = false,
        onPrefsChange = { dummyPrefs = it },
        onSave = { }
    )
}