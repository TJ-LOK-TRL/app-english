// MainActivity.kt
package com.masterproject.englishapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.masterproject.englishapp.permissions.PermissionManager
import com.masterproject.englishapp.recorder.AndroidAudioRecorder
import com.masterproject.englishapp.screens.MainLandingPage
import com.masterproject.englishapp.ui.theme.MyApplicationTheme

/**
 * MainActivity - Entry point of the English Learning Application
 */
class MainActivity : ComponentActivity() {

    // Initialize recorder here - it's the app-level dependency
    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionManager = PermissionManager(this)

        // Set up Compose UI
        setContent {
            MyApplicationTheme {
                MainLandingPage(
                    permissionManager = permissionManager,
                    recorder = recorder
                )
            }
        }
    }
}