package com.masterproject.englishapp.screens.exercises.speakphrase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.audio.encodeWaveToBytes
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.Circular3DButton
import com.masterproject.englishapp.exercises.SpeakPhraseData
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.network.RetrofitClient
import com.masterproject.englishapp.network.model.PronunciationResult
import com.masterproject.englishapp.network.model.WordResult
import com.masterproject.englishapp.recorder.AudioRecorder
import com.masterproject.englishapp.ui.theme.AppColors
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun SpeakPhraseContent(
    data: SpeakPhraseData,
    onResult: (ExerciseResult) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isRecording by remember { mutableStateOf(false) }
    var wordResults by remember { mutableStateOf<List<WordResult>?>(null) }
    var statusText by remember { mutableStateOf("Ready to record") }
    var isLoading by remember { mutableStateOf(false) }

    val annotatedPhrase = buildAnnotatedString {
        val words = data.learningPhrase.split(" ")
        words.forEachIndexed { index, word ->
            val result = wordResults?.getOrNull(index)
            val color = when (result?.label) {
                "passed" -> Color(0xFF4CAF50)  // Green
                "average" -> Color(0xFFFFC107) // Yellow
                "failed" -> Color(0xFFF44336)  // Red
                else -> AppColors.Black800     // Default
            }
            withStyle(style = SpanStyle(color = color, fontWeight = FontWeight.Bold)) {
                append(word)
            }
            if (index < words.size - 1) append(" ")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, end = 16.dp, top = 0.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIcon(
                resId = R.drawable.dragon_point_up_small,
                size = 96.dp,
                flipHorizontal = true
            )
            Bubble(text = "Say the sentences!", side = Side.Left, textColor = AppColors.Black800)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(resId = R.drawable.ic_two_people_talking_3, size = 300.dp)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Text(
                    text = annotatedPhrase,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Black800,
                    fontSize = 22.sp
                )

                Text(
                    text = if (isRecording) "Listening..." else statusText,
                    fontSize = 18.sp,
                    color = if (isRecording) AppColors.Primary else AppColors.Black600
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Can't speak now",
                    fontSize = 16.sp,
                    color = AppColors.Gray600,
                    modifier = Modifier.clickable {
                        onResult(ExerciseResult.Skipped())
                    }
                )
            }
        }

        Row {
            Circular3DButton(
                size = 72.dp,
                color = AppColors.Primary,
                depth = 3.dp,
                contentOffsetX = 2.dp,
                onClick = {
                    if (isRecording) {
                        val finalArray = data.recorder.stopRecording()
                        isRecording = false

                        coroutineScope.launch {
                            try {
                                val audioBytes = encodeWaveToBytes(finalArray!!)
                                val requestFile = audioBytes.toRequestBody("audio/wav".toMediaType())
                                val audioPart = MultipartBody.Part.createFormData(
                                    "audio",
                                    "recording.wav",
                                    requestFile
                                )

                                statusText = "Enviando pedido!"
                                isLoading = true
                                val result = RetrofitClient.api.evaluatePronunciation(
                                    audio = audioPart,
                                    targetText = data.learningPhrase.toRequestBody()
                                )

                                wordResults = result.results

                                wordResults?.let {
                                    val passed = hasPassedPronunciation(
                                        result = PronunciationResult(it)
                                    )

                                    onResult(ExerciseResult.fromBool(passed))
                                }

                                statusText = "Avaliação concluída!"

                            } catch (e: Exception) {
                                statusText = "Erro na avaliação: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        data.recorder.startRecording { }
                        isRecording = true
                        statusText = "A gravar..."
                    }
                }
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    isRecording -> {
                        AppIcon(
                            resId = R.drawable.ic_microphone_cutted,
                            size = 32.dp,
                            tint = Color.White
                        )
                    }

                    else -> {
                        AppIcon(
                            resId = R.drawable.ic_microphone,
                            size = 32.dp,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Xiaomi Redmi 9C",
    device = "spec:width=360dp,height=800dp,dpi=269",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEEEEEE
)
@Composable
fun SpeakPhraseScreenPreview() {
    SpeakPhraseContent(
        data = SpeakPhraseData(
            learningPhrase = "My name is Maria.",
            feedbackPhrase = "Meu nome é Maria.",
            recorder = object : AudioRecorder {
                override fun startRecording(onAudioData: (FloatArray) -> Unit) { }

                override fun stopRecording(): FloatArray? {
                    return null
                }

                override val isRecording: Boolean
                    get() = false
            }
        ),
        onResult = { }
    )
}

fun hasPassedPronunciation(
    result: PronunciationResult,
    minAverageScore: Float = 0.5f
): Boolean {
    if (result.results.isEmpty()) return false

    val averageScore = result.results
        .map { it.score }
        .average()
        .toFloat()

    val hasFailedWord = result.results.any { it.label == "failed" }

    return averageScore >= minAverageScore && !hasFailedWord
}