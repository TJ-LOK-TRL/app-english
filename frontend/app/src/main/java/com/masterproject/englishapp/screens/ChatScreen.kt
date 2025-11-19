// screens/ChatScreen.kt
package com.masterproject.englishapp.screens

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.google.gson.Gson
import com.masterproject.englishapp.audio.encodeWaveToBytes
import com.masterproject.englishapp.network.RetrofitClient
import com.masterproject.englishapp.recorder.AudioRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.util.Base64
import android.util.Log
import kotlin.math.roundToInt
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ChatScreen(
    recorder: AudioRecorder
) {
    val inputText = remember { mutableStateOf("") }
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    // BOX permite sobreposição (WebView no fundo, controles por cima)
    androidx.compose.foundation.layout.Box(Modifier.fillMaxSize()) {

        // === WebView (fundo) ===
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

                    webChromeClient = object : android.webkit.WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: android.webkit.ConsoleMessage): Boolean {
                            android.util.Log.d(
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
            modifier = Modifier.fillMaxSize() // ocupa a tela inteira
        )

        // === Controles (sobrepostos) ===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(androidx.compose.ui.Alignment.BottomCenter)
        ) {
            // Recorder instance
            val isRecording = remember { mutableStateOf(false) }

            Button(
                onClick = {
                    if (isRecording.value) {
                        // Stop recording
                        isRecording.value = false
                        val wavFloats = recorder.stopRecording()
                        if (wavFloats != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val wavBytes = encodeWaveToBytes(wavFloats, 16000)
                                    val data = handleConverseData(wavBytes)
                                    withContext(Dispatchers.Main) {
                                        val jsonStr = Gson().toJson(data)
                                        val safeJson = JSONObject.quote(jsonStr)
                                        webViewRef.value?.evaluateJavascript(
                                            "window.speakAudio($safeJson);", null
                                        )
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    } else {
                        // Start recording
                        isRecording.value = true
                        recorder.startRecording { }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(if (isRecording.value) "Stop Recording" else "Start Recording")
            }
        }
    }
}

suspend fun handleConverseData(
    wavBytes: ByteArray,
    lang: String? = null,
    voice: String? = null,
    speed: String? = null
): TalkingHeadData {
    val response = RetrofitClient.api.converse(
        audio = MultipartBody.Part.createFormData(
            "audio", "speech.wav", wavBytes.toRequestBody("audio/wav".toMediaTypeOrNull())
        ),
        lang = lang?.toRequestBody("text/plain".toMediaTypeOrNull()),
        voice = voice?.toRequestBody("text/plain".toMediaTypeOrNull()),
        speed = speed?.toRequestBody("text/plain".toMediaTypeOrNull())
    )

    val audioBytes = Base64.decode(response.audio, Base64.DEFAULT)
    val audioBase64 = Base64.encodeToString(audioBytes, Base64.NO_WRAP)
    val tokens = response.tokens
    val predDur = response.predDur

    val audioBuffer = ByteBuffer.wrap(audioBytes, 44, audioBytes.size - 44)
        .order(ByteOrder.LITTLE_ENDIAN)
        .asShortBuffer()
    val audioShorts = ShortArray(audioBuffer.remaining())
    audioBuffer.get(audioShorts)

    val frameToMs = { f: Int -> (f * 1000 / 24) }

    var predIndex = 1
    val visemes = mutableListOf<String>()
    val vtimes = mutableListOf<Int>()
    val vdurations = mutableListOf<Int>()

    for (token in tokens) {
        if (token.phonemes.isNullOrEmpty()) {
            if (token.whitespace) predIndex++
            continue
        }

        for (ph in token.phonemes.toCharArray().map { it.toString() }) {
            val durFrames = predDur.getOrElse(predIndex) { 0f }.roundToInt()
            val durMs = frameToMs(durFrames)
            val startMs = if (vtimes.isNotEmpty())
                vtimes.last() + vdurations.last()
            else
                frameToMs(predDur.getOrElse(predIndex - 1) { 0f }.roundToInt())
            val mapped = misakiToOculusViseme[ph]
            if (mapped != null) {
                visemes.add(mapped)
                vtimes.add(startMs)
                vdurations.add(durMs)
            }
            predIndex++
        }

        if (token.whitespace) predIndex++
    }

    val filteredTokens = tokens.filter {
        it.text.trim().isNotEmpty() && !it.text.contains('<')
    }

    val words = filteredTokens.map { it.text }
    val wtimes = filteredTokens.map { (it.startTs * 1000).toInt() }
    val wdurations = filteredTokens.map { ((it.endTs - it.startTs) * 1000).toInt() }

    val audioDurationMs = (audioBytes.size - 44) / (24000 * 2 / 1000)
    val lastVEnd = if (vtimes.isNotEmpty()) vtimes.last() + vdurations.last() else 0

    if (lastVEnd != 0 && kotlin.math.abs(lastVEnd - audioDurationMs) != 0) {
        val scale = audioDurationMs.toFloat() / lastVEnd
        for (i in vtimes.indices) {
            vtimes[i] = Math.round(vtimes[i] * scale)
            vdurations[i] = Math.round(vdurations[i] * scale)
        }
    }

    return TalkingHeadData(
        words = words,
        wtimes = wtimes,
        wdurations = wdurations,
        visemes = visemes,
        vtimes = vtimes,
        vdurations = vdurations,
        audio = audioBase64,
        audioEncoding = "wav"
    )
}

data class TalkingHeadData(
    val words: List<String>,
    val wtimes: List<Int>,
    val wdurations: List<Int>,
    val visemes: List<String>,
    val vtimes: List<Int>,
    val vdurations: List<Int>,
    val audio: String,
    val audioEncoding: String
)

private val misakiToOculusViseme = mapOf(
    "$" to null, ";" to null, ":" to null, "," to null, "." to null, "!" to null, "?" to null,
    "—" to null, "…" to null, "\"" to null, "(" to null, ")" to null, "“" to null, "”" to null,
    " " to null, "\u0303" to null, "ʣ" to "DD", "ʥ" to "CH", "ʦ" to "CH", "ʨ" to "CH",
    "ᵝ" to null, "ꭧ" to null, "A" to "E", "I" to "I", "O" to "O", "Q" to "O", "S" to "SS",
    "T" to "DD", "W" to "U", "Y" to "I", "ᵊ" to null, "a" to "aa", "b" to "PP", "c" to "kk",
    "d" to "DD", "e" to "E", "f" to "FF", "h" to null, "i" to "I", "j" to "I", "k" to "kk",
    "l" to "RR", "m" to "PP", "n" to "nn", "o" to "O", "p" to "PP", "q" to "kk", "r" to "RR",
    "s" to "SS", "t" to "DD", "u" to "U", "v" to "FF", "w" to "U", "x" to "SS", "y" to "I",
    "z" to "SS", "ɑ" to "aa", "ɐ" to "aa", "ɒ" to "aa", "æ" to "aa", "β" to "FF", "ɔ" to "O",
    "ɕ" to "SS", "ç" to "SS", "ɖ" to "DD", "ð" to "TH", "ʤ" to "CH", "ə" to "E", "ɚ" to "RR",
    "ɛ" to "E", "ɜ" to "E", "ɟ" to "DD", "ɡ" to "kk", "ɥ" to "U", "ɨ" to "I", "ɪ" to "I",
    "ʝ" to "I", "ɯ" to "U", "ɰ" to "U", "ŋ" to "nn", "ɳ" to "nn", "ɲ" to "nn", "ɴ" to "nn",
    "ø" to "O", "ɸ" to "FF", "θ" to "TH", "œ" to "E", "ɹ" to "RR", "ɾ" to "DD", "ɻ" to "RR",
    "ʁ" to "RR", "ɽ" to "RR", "ʂ" to "SS", "ʃ" to "SS", "ʈ" to "DD", "ʧ" to "CH",
    "ʊ" to "U", "ʋ" to "FF", "ʌ" to "aa", "ɣ" to null, "ɤ" to "O", "χ" to null, "ʎ" to "RR",
    "ʒ" to "SS", "ʔ" to null, "ˈ" to null, "ˌ" to null, "ː" to null, "ʰ" to null, "ʲ" to null,
    "↓" to null, "→" to null, "↗" to null, "↘" to null, "ᵻ" to "I"
)