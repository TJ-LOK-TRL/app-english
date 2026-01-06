package com.masterproject.englishapp.screens.account.preferences

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
fun PreferencesScreen(
    viewModel: PreferencesViewModel = hiltViewModel(),
) {
    val user by viewModel.userState.collectAsState()
    var isSaving by remember { mutableStateOf(false) }

    if (user != null) {
        var draftPrefs by remember(user) { mutableStateOf(user!!.preferences) }

        Box(modifier = Modifier.fillMaxSize()) {
            PreferencesScreenContent(
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
fun PreferencesScreenContent(
    preferences: UserPreferences,
    isSaving: Boolean,
    onPrefsChange: (UserPreferences) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Rect3DCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                SettingToggleField(
                    label = "Sound Effects",
                    checked = preferences.soundEffectsEnabled,
                    onCheckedChange = {
                        onPrefsChange(preferences.copy(soundEffectsEnabled = it))
                    }
                )

                SettingToggleField(
                    label = "Vibration",
                    checked = preferences.vibrationEnabled,
                    onCheckedChange = {
                        onPrefsChange(preferences.copy(vibrationEnabled = it))
                    }
                )
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
fun PreferencesScreenPreview() {
    var dummyPrefs by remember { mutableStateOf(DummyUserPreferences) }

    PreferencesScreenContent(
        preferences = dummyPrefs,
        isSaving = false,
        onPrefsChange = { dummyPrefs = it },
        onSave = { }
    )
}