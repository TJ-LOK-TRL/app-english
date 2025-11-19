// screens/MainLandingPage.kt
package com.masterproject.englishapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.masterproject.englishapp.components.CommonHeader
import com.masterproject.englishapp.navigation.MainNavigation
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.permissions.PermissionManager
import com.masterproject.englishapp.recorder.AndroidAudioRecorder

@Composable
fun MainLandingPage(
    permissionManager: PermissionManager,
    recorder: AndroidAudioRecorder
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CommonHeader(
                navController = navController,
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MainNavigation(
                navController = navController,
                permissionManager = permissionManager,
                recorder = recorder
            )
        }
    }
}