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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.audio.encodeWaveToBytes
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.SpeakLayout
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
import com.masterproject.englishapp.utils.DummyAudioRecorder
import com.masterproject.englishapp.utils.extractSkillIds
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun SpeakPhraseContent(data: SpeakPhraseData, onResult: (ExerciseResult) -> Unit) {
    SpeakLayout(
        recorder = data.recorder,
        skillIds = data.learningPhrase.extractSkillIds(),
        targetText = data.learningPhrase,
        feedbackText = data.feedbackPhrase,
        instruction = "Say the sentences!",
        onResult = onResult
    ) { annotatedText ->
        AppIcon(resId = R.drawable.ic_two_people_talking_3, size = 300.dp)
        Text(text = annotatedText, fontSize = 22.sp, textAlign = TextAlign.Center)
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
            recorder = DummyAudioRecorder
        ),
        onResult = { }
    )
}