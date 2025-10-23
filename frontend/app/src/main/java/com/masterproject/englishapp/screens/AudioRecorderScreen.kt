// AudioRecorderScreen.kt
package com.masterproject.englishapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.audio.encodeWaveToBytes
import com.masterproject.englishapp.network.RetrofitClient
import com.masterproject.englishapp.recorder.AudioRecorder
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.masterproject.englishapp.network.WordResult

@Composable
fun AudioRecorderScreen(
    isPermissionGranted: Boolean,
    recorder: AudioRecorder
) {
    var isRecording by remember { mutableStateOf(false) }
    var wordResults by remember { mutableStateOf<List<WordResult>?>(null) }
    var statusText by remember { mutableStateOf("Pronto para gravar") }
    val coroutineScope = rememberCoroutineScope()

    val targetText = "Tomorrow I will go to the school and I will study."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top left text
        Text(
            text = "Repete o que ouves",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Target phrase with gray border (colored words after API)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(12.dp))
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (wordResults == null) {
                Text(text = targetText, fontSize = 18.sp)
            } else {
                val words = targetText.split(" ")
                val annotatedText = buildAnnotatedString {
                    words.forEachIndexed { index, word ->
                        val result = wordResults?.getOrNull(index)
                        val color = when (result?.label) {
                            "passed" -> Color(0xFF4CAF50)  // Green
                            "average" -> Color(0xFFFFC107) // Yellow
                            "failed" -> Color(0xFFF44336)  // Red
                            else -> Color.Unspecified
                        }
                        withStyle(style = SpanStyle(color = color)) {
                            append(word)
                        }
                        append(" ")
                    }
                }
                BasicText(
                    text = annotatedText,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // Status text (feedback or score)
        Text(
            text = statusText,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        // Big microphone button
        Button(
            onClick = {
                if (isRecording) {
                    val finalArray = recorder.stopRecording()
                    isRecording = false
                    statusText = "A enviar para API..."

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
                                targetText = targetText.toRequestBody()
                            )

                            // Guardar resultados para colorir as palavras
                            wordResults = result.results

                            statusText = "✅ Avaliação concluída!"

                        } catch (e: Exception) {
                            statusText = "❌ Erro: ${e.message}"
                        }
                    }
                } else {
                    recorder.startRecording { }
                    isRecording = true
                    statusText = "A gravar..."
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                painter = painterResource(R.drawable.mic),
                contentDescription = "Microfone"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (isRecording) "Parar gravação" else "Clique para falar",
                fontSize = 18.sp
            )
        }
    }
}
