package com.masterproject.englishapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.masterproject.englishapp.components.CommonHeader
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.recorder.AndroidAudioRecorder

// MainLandingPage.kt
@Composable
fun MainLandingPage(
    isPermissionGranted: Boolean,
    recorder: AndroidAudioRecorder,
    onNavigateBack: () -> Unit = {},
    currentScreen: Screen = Screen.HOME
) {
    val navigationState = rememberNavController()

    Scaffold(
        topBar = {
            CommonHeader(
                title = when (currentScreen) {
                    Screen.HOME -> "English Learning"
                    Screen.RECORDER -> "Voice Recorder"
                    Screen.PRACTICE -> "Practice"
                    Screen.PROFILE -> "Profile"
                },
                onBackClick = if (currentScreen != Screen.HOME) {
                    onNavigateBack
                } else {
                    null // No back button on home screen
                },
                showBackButton = currentScreen != Screen.HOME
            )
        }
    ) { innerPadding ->
        // Your screen content here
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.HOME -> AudioRecorderScreen(isPermissionGranted, recorder) //HomeScreen()
                Screen.RECORDER -> AudioRecorderScreen(isPermissionGranted, recorder)
                Screen.PRACTICE -> AudioRecorderScreen(isPermissionGranted, recorder) //PracticeScreen()
                Screen.PROFILE -> AudioRecorderScreen(isPermissionGranted, recorder) //ProfileScreen()
            }
        }
    }
}