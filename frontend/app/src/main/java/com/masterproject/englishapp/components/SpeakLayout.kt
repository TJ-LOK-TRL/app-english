package com.masterproject.englishapp.components

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
import androidx.compose.ui.text.AnnotatedString
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
import com.masterproject.englishapp.exercises.pronunciation.calculatePronunciationSuccess
import com.masterproject.englishapp.network.RetrofitClient
import com.masterproject.englishapp.network.model.PronunciationResult
import com.masterproject.englishapp.network.model.WordResult
import com.masterproject.englishapp.recorder.AudioRecorder
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.utils.DummyAudioRecorder
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun SpeakLayout(
    recorder: AudioRecorder,
    skillIds: List<String>,
    targetText: String,
    feedbackText: String,
    instruction: String,
    onResult: (ExerciseResult) -> Unit,
    showStatusText: Boolean = false,
    showCantSpeakText: Boolean = true,
    middleContent: @Composable (AnnotatedString) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isRecording by remember { mutableStateOf(false) }
    var wordResults by remember { mutableStateOf<List<WordResult>?>(null) }
    var statusText by remember { mutableStateOf("Ready to record") }
    var isLoading by remember { mutableStateOf(false) }

    // Color Logic
    val annotatedPhrase = buildAnnotatedString {
        val words = targetText.split(" ")
        words.forEachIndexed { index, word ->
            val result = wordResults?.getOrNull(index)
            val color = when (result?.label) {
                "passed" -> Color(0xFF4CAF50)
                "average" -> Color(0xFFFFC107)
                "failed" -> Color(0xFFF44336)
                else -> AppColors.Black800
            }
            withStyle(style = SpanStyle(color = color, fontWeight = FontWeight.Bold)) {
                append(word)
            }
            if (index < words.size - 1) append(" ")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Common Top (Dragon + Instruction)
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppIcon(resId = R.drawable.dragon_point_up_small, size = 96.dp, flipHorizontal = true)
            Bubble(text = instruction, side = Side.Left, textColor = AppColors.Black800)
        }

        // Middle Content
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            middleContent(annotatedPhrase)

            if (showStatusText || showCantSpeakText) {
                Spacer(modifier = Modifier.height(12.dp))

                if (showStatusText) {
                    Text(
                        text = if (isRecording) "Listening..." else statusText,
                        fontSize = 18.sp,
                        color = if (isRecording) AppColors.Primary else AppColors.Black600
                    )
                }

                if (showCantSpeakText) {
                    Text(
                        text = "Can't speak now",
                        modifier = Modifier.padding(top = 16.dp).clickable { onResult(ExerciseResult.Skipped()) },
                        color = AppColors.Gray600
                    )
                }
            }
        }

        // Button to start listening and stop as well
        Circular3DButton(
            size = 72.dp,
            color = if (isRecording) Color.Red else AppColors.Primary,
            enabled = !isLoading,
            onClick = {
                if (isRecording) {
                    val finalArray = recorder.stopRecording()
                    isRecording = false
                    coroutineScope.launch {
                        try {
                            isLoading = true
                            statusText = "Analyzing..."
                            val audioBytes = encodeWaveToBytes(finalArray!!)
                            val audioPart = MultipartBody.Part.createFormData(
                                "audio", "recording.wav",
                                audioBytes.toRequestBody("audio/wav".toMediaType())
                            )

                            val result = RetrofitClient.api.evaluatePronunciation(
                                audio = audioPart,
                                targetText = targetText.toRequestBody()
                            )

                            wordResults = result.results
                            val passed = calculatePronunciationSuccess(PronunciationResult(result.results))

                            val translation = "Significado: $feedbackText"
                            onResult(
                                ExerciseResult.fromBool(
                                    skillIds = skillIds,
                                    isCorrect = passed,
                                    correctMessage = translation,
                                    wrongMessage = translation
                                )
                            )

                            statusText = if(passed) "Great job!" else "Try again!"
                        } catch (e: Exception) {
                            statusText = "Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    recorder.startRecording { }
                    isRecording = true
                    wordResults = null
                }
            }
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(32.dp))
            else AppIcon(
                resId = if (isRecording) R.drawable.ic_microphone_cutted else R.drawable.ic_microphone,
                size = 32.dp, tint = Color.White
            )
        }
    }
}