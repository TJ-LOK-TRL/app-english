package com.masterproject.englishapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.masterproject.englishapp.recorder.AndroidAudioRecorder
import com.masterproject.englishapp.AudioRecorderScreen
import com.masterproject.englishapp.ui.theme.MyApplicationTheme
import com.whispercpp.whisper.WhisperContext
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted. Recording can proceed.
            // The Compose state will be updated accordingly.
        } else {
            // Permission denied. Inform the user.
            // TODO: Replace this simple Toast with a more robust UI explanation
            // using a Snackbar or a Dialog in a real application.
            // 'context' needs to be available in this scope, e.g., passed to the function or available via an Application/Activity context.
            Toast.makeText(this, "Permission denied. Cannot proceed without permission.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica e solicita a permissão de gravação se necessário
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (!isPermissionGranted) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    //BakingScreen()
                    AudioRecorderScreen(
                        isPermissionGranted = isPermissionGranted,
                        recorder = recorder
                    )
                }
            }
        }
    }
}