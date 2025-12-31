// MainActivity.kt
package com.masterproject.englishapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.masterproject.englishapp.permissions.PermissionManager
import com.masterproject.englishapp.recorder.AndroidAudioRecorder
import com.masterproject.englishapp.screens.main.MainLandingPage
import com.masterproject.englishapp.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - Entry point of the English Learning Application
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionManager = PermissionManager(this)

        // Set up Compose UI
        setContent {
            MyApplicationTheme {
                MainLandingPage(
                    permissionManager = permissionManager,
                )
            }
        }
    }
}