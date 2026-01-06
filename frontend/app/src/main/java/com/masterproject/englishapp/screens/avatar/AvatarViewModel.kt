package com.masterproject.englishapp.screens.avatar

import android.util.Base64
import android.util.Log
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.masterproject.englishapp.audio.encodeWaveToBytes
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.network.RetrofitClient
import com.masterproject.englishapp.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt

@HiltViewModel
class AvatarViewModel @Inject constructor(
    val recorder: AudioRecorder,
    val uiEventService: UiEventService
): ViewModel() {
    suspend fun processAudio(webView: WebView?): Long {
        val wavFloats = recorder.stopRecording() ?: run {
            Log.e("AvatarViewModel", "Stop recording returned null wav floats")
            return 0L
        }
        return try {
            val wavBytes = encodeWaveToBytes(wavFloats, 16000)
            val data = handleConverseData(wavBytes)
            withContext(Dispatchers.Main) {
                val jsonStr = Gson().toJson(data)
                val safeJson = JSONObject.quote(jsonStr)
                webView?.evaluateJavascript("window.speakAudio($safeJson);", null)
            }

            // Estimate audio duration
            val audioDurationMs = (data.audio.length * 3 / 4 / (24000 * 2 / 1000)).toLong()
            val waitTime = audioDurationMs + 2000L // Safe extra of 2s
            waitTime
        } catch (e: Exception) {
            uiEventService.showError(e.localizedMessage ?: "Unknown Error")
            Log.e("AvatarViewModel", "Error processing audio", e)
            0L
        }
    }

    private suspend fun handleConverseData(
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

        if (lastVEnd != 0 && abs(lastVEnd - audioDurationMs) != 0) {
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