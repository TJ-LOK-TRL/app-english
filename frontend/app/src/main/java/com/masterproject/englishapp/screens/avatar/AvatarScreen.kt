// screens/AvatarScreen.kt
package com.masterproject.englishapp.screens.avatar

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewAssetLoader
import com.masterproject.englishapp.recorder.AudioRecorder
import com.masterproject.englishapp.R
import kotlinx.coroutines.launch
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun AvatarScreen(
    viewModel: AvatarViewModel = hiltViewModel()
) {
    AvatarScreenContent(
        recorder = viewModel.recorder,
        processAudio = { webview -> viewModel.processAudio(webview) }
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AvatarScreenContent(
    recorder: AudioRecorder,
    processAudio: suspend (WebView?) -> Long,
) {
    val scope = rememberCoroutineScope()
    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    val isAvatarSpeaking = remember { mutableStateOf(false) }

    val lastActiveTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val isRecording = remember { mutableStateOf(false) }
    val silenceThreshold = 0.1f
    val manualMode = remember { mutableStateOf(true) }
    val hasSpoken = remember { mutableStateOf(false) }
    val currentAmplitude = remember { mutableFloatStateOf(0f) }
    val timeSinceLastActive = remember { mutableLongStateOf(0L) }
    val isLoopActive = remember { mutableStateOf(false) }

    val startAutoRecording = {
        lastActiveTime.longValue = System.currentTimeMillis()
        recorder.startRecording { audioArray ->
            if (!isAvatarSpeaking.value) {
                val maxAmplitude = audioArray.maxOfOrNull { abs(it) } ?: 0f
                currentAmplitude.value = maxAmplitude // For UI

                if (maxAmplitude > 0.01f) {
                    Log.d("AudioLevel", "Peak: $maxAmplitude | HasSpoken: ${hasSpoken.value}")
                }

                if (maxAmplitude > silenceThreshold) {
                    lastActiveTime.longValue = System.currentTimeMillis()
                    hasSpoken.value = true
                }
            }
        }
    }

    LaunchedEffect(manualMode.value) {
        if (!manualMode.value) {
            // Reset state and initialize flags for Auto Mode
            isLoopActive.value = true
            isRecording.value = true
            hasSpoken.value = false
            timeSinceLastActive.longValue = 0L

            // Start the initial hardware recording session
            startAutoRecording()

            // Main monitoring loop: stays active while in Auto Mode
            while (isLoopActive.value && !manualMode.value) {
                delay(50) // Small delay to prevent CPU spiking while allowing smooth UI updates
                val now = System.currentTimeMillis()
                val diff = now - lastActiveTime.longValue
                timeSinceLastActive.longValue = diff

                // Check if the silence duration has exceeded the 2.5s threshold
                if (diff > 2500) {
                    Log.d("AVATAR_DEBUG", "Silence threshold reached (2.5s)")

                    if (hasSpoken.value) {
                        Log.d("AVATAR_DEBUG", "Voice was detected; stopping recorder and processing...")

                        // Update UI to "Thinking/Speaking" state
                        isRecording.value = false

                        isAvatarSpeaking.value = true
                        // processAudio returns the estimated duration of the avatar's speech
                        val waitTime = processAudio(webViewRef.value)
                        // Wait for the avatar to finish speaking before resuming listen mode
                        delay(waitTime)
                        isAvatarSpeaking.value = false
                    } else {
                        Log.d("AVATAR_DEBUG", "No voice detected; clearing buffer to remove background noise")
                        recorder.stopRecording()
                    }

                    // Prepare and restart the recording cycle if still in Auto Mode
                    if (!manualMode.value) {
                        hasSpoken.value = false
                        timeSinceLastActive.longValue = 0L
                        lastActiveTime.longValue = System.currentTimeMillis()

                        startAutoRecording()
                        isRecording.value = true
                    }
                    // Break the current while loop iteration to ensure a clean restart in the next cycle
                    break
                }
            }
        } else {
            // Cleanup: Stop all recording and reset timers when switching to Manual Mode
            isLoopActive.value = false
            isRecording.value = false
            recorder.stopRecording()
            timeSinceLastActive.longValue = 0L
        }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.img_avatar_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        AndroidView(
            factory = { context ->
                val assetLoader = WebViewAssetLoader.Builder()
                    .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
                    .build()

                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {
                            request?.url?.let { return assetLoader.shouldInterceptRequest(it) }
                            return null
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                            Log.d(
                                "WebViewJS",
                                "${consoleMessage.message()} (Line ${consoleMessage.lineNumber()})"
                            )
                            return true
                        }
                    }

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    settings.allowUniversalAccessFromFileURLs = true
                    settings.allowFileAccessFromFileURLs = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                    setBackgroundColor(0)
                    setLayerType(WebView.LAYER_TYPE_HARDWARE, null) // Improve performance

                    loadUrl("https://appassets.androidplatform.net/assets/web/avatar3D/index.html")

                    webViewRef.value = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Auto", color = Color.White)
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = manualMode.value,
                onCheckedChange = {
                    manualMode.value = it
                    isRecording.value = false
                },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Cyan)
            )
            Spacer(Modifier.width(8.dp))
            Text("Manual", color = Color.White)
        }

        if (manualMode.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Button(
                    onClick = {
                        if (isRecording.value) {
                            // Stop recording
                            isRecording.value = false
                            scope.launch {
                                isAvatarSpeaking.value = true
                                val waitTime = processAudio(webViewRef.value)
                                delay(waitTime)
                                isAvatarSpeaking.value = false
                            }
                        } else {
                            // Start recording
                            isRecording.value = true
                            lastActiveTime.longValue = System.currentTimeMillis()
                            recorder.startRecording { /* This must be empty, all manual */ }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = !isAvatarSpeaking.value
                ) {
                    Text(if (isRecording.value) "Stop Recording" else "Start Recording")
                }
            }
        } else {
            if (isRecording.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(currentAmplitude.value.coerceIn(0f, 1f))
                            .height(6.dp)
                            .background(
                                if (hasSpoken.value) Color.Green else Color.Yellow,
                                shape = RoundedCornerShape(bottomEnd = 4.dp)
                            )
                    )

                    val timeLeft = ((2500L - timeSinceLastActive.longValue).coerceAtLeast(0L)) / 1000.0
                    Text(
                        text = when {
                            isAvatarSpeaking.value -> "Thinking..."
                            !isRecording.value -> "Processing..."
                            hasSpoken.value && timeSinceLastActive.longValue > 1000 -> String.format("Finishing in %.1fs...", timeLeft)
                            hasSpoken.value -> "Listening..."
                            else -> "Say something..."
                        },
                        modifier = Modifier.padding(top = 8.dp),
                        color = when {
                            isAvatarSpeaking.value -> Color.Cyan
                            hasSpoken.value && timeSinceLastActive.longValue > 1200 -> Color(0xFFFFA500)
                            hasSpoken.value -> Color.Green
                            else -> Color.White
                        },
                        style = TextStyle(
                            shadow = Shadow(blurRadius = 8f),
                            fontWeight = FontWeight.Bold
                        )
                    )

                    if (hasSpoken.value && timeSinceLastActive.longValue > 1200 && !isAvatarSpeaking.value) {
                        Text(
                            "Speak to continue...",
                            style = TextStyle(fontSize = 12.sp),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}