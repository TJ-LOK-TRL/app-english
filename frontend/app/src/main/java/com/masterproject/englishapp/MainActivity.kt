// MainActivity.kt
package com.masterproject.englishapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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

    private var isAudioPermissionGranted = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        isAudioPermissionGranted = isGranted
        if (isGranted) {
            // Permission granted - recorder is ready to use
        } else {
            Toast.makeText(
                this,
                "Audio recording permission is required for voice features.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check audio permission status
        isAudioPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        // Request permission if not granted
        if (!isAudioPermissionGranted) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        // Set up Compose UI
        setContent {
            MyApplicationTheme {
                // Navigate to MainLandingPage instead of AudioRecorderScreen
                MainLandingPage(
                    isPermissionGranted = isAudioPermissionGranted,
                    recorder = recorder
                )
            }
        }
    }
}