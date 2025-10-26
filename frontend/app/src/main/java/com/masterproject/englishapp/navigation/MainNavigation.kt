// navigation/MainNavigation.kt
package com.masterproject.englishapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.masterproject.englishapp.recorder.AndroidAudioRecorder
import com.masterproject.englishapp.screens.AudioRecorderScreen
import com.masterproject.englishapp.screens.ChatScreen
import com.masterproject.englishapp.screens.HomeScreen

@Composable
fun MainNavigation(
    navController: NavHostController,
    isPermissionGranted: Boolean,
    recorder: AndroidAudioRecorder
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HOME.route
    ) {
        composable(Screen.HOME.route) {
            HomeScreen(
                onNavigate = { screen ->
                    navController.navigate(screen.route)
                }
            )
        }
        composable(Screen.RECORDER.route) {
            AudioRecorderScreen(
                isPermissionGranted = isPermissionGranted,
                recorder = recorder,
            )
        }
        composable(Screen.PRACTICE.route) {
            //PracticeScreen(
            //    onNavigateBack = { navController.navigateUp() }
            //)
        }
        composable(Screen.PROFILE.route) {
            //ProfileScreen(
            //    onNavigateBack = { navController.navigateUp() }
            //)
        }

        composable(Screen.CHAT.route) {
            ChatScreen()
        }
    }
}