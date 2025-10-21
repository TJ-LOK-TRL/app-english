// BakingScreen.kt
package com.masterproject.englishapp

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.audio.encodeWaveToBytes
import com.masterproject.englishapp.network.ApiService
import com.masterproject.englishapp.network.RetrofitClient
import com.masterproject.englishapp.recorder.AudioRecorder
import com.whispercpp.whisper.WhisperContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun AudioRecorderScreen(
    isPermissionGranted: Boolean,
    recorder: AudioRecorder
) {
    var isRecording by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("Ready to record") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                if (isRecording) {
                    val finalArray = recorder.stopRecording()
                    isRecording = false
                    statusText = "Sending to API..."

                    coroutineScope.launch {
                        try {
                            val audioBytes = encodeWaveToBytes(finalArray!!)
                            val requestFile = audioBytes.toRequestBody("audio/wav".toMediaType())
                            val audioPart = MultipartBody.Part.createFormData(
                                "audio",
                                "recording.wav",
                                requestFile
                            )

                            val result = RetrofitClient.api.evaluatePronunciation(
                                audio = audioPart,
                                targetText = "Tomorrow I will go to the school and I will study.".toRequestBody()
                            )

                            statusText = "✅ Score: ${result.score}\n${result.feedback}"

                        } catch (e: Exception) {
                            statusText = "❌ Error: ${e.message}"
                        }
                    }
                } else {
                    recorder.startRecording { }
                    isRecording = true
                    statusText = "Recording..."
                }
            }
        ) {
            Text(if (isRecording) "STOP" else "RECORD")
        }

        Text(text = statusText)
    }
}