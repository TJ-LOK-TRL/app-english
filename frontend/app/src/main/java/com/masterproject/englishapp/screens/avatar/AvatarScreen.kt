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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
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

    LaunchedEffect(manualMode.value) {
        if (!manualMode.value) {
            // If not manual, init the recording
            isRecording.value = true
            lastActiveTime.longValue = System.currentTimeMillis()

            recorder.startRecording { audioArray ->
                if (!isAvatarSpeaking.value) {
                    val amplitude = audioArray.map { abs(it) }.average()
                    if (amplitude > silenceThreshold) {
                        lastActiveTime.longValue = System.currentTimeMillis()
                    }
                }
            }
        }
    }

    LaunchedEffect(isRecording.value) {
        if (isRecording.value) {
            while (isRecording.value) {
                delay(100)
                val currentTime = System.currentTimeMillis()

                if (currentTime - lastActiveTime.longValue > 2000) {
                    isRecording.value = false

                    scope.launch {
                        isAvatarSpeaking.value = true
                        val timeToWait = processAudio(webViewRef.value)
                        delay(timeToWait)
                        isAvatarSpeaking.value = false

                        if (!manualMode.value) {
                            lastActiveTime.longValue = System.currentTimeMillis()
                            isRecording.value = true
                            recorder.startRecording { audioArray ->
                                var sum = 0f
                                for (sample in audioArray) {
                                    sum += abs(sample)
                                }
                                val amplitude = sum / audioArray.size

                                if (amplitude > silenceThreshold) {
                                    lastActiveTime.longValue = System.currentTimeMillis()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
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

                    loadUrl("https://appassets.androidplatform.net/assets/web/avatar3D/index.html")

                    webViewRef.value = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

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
                Text(
                    "Listening...",
                    modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
                    color = Color.Green
                )
            }
        }
    }
}