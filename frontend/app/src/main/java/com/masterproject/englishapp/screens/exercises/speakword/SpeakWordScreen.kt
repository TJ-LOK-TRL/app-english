package com.masterproject.englishapp.screens.exercises.speakword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.SpeakLayout
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.Circular3DButton
import com.masterproject.englishapp.components.optiongrid.TextImageAnswerOption
import com.masterproject.englishapp.components.tokenImagePainter
import com.masterproject.englishapp.grammar.Category
import com.masterproject.englishapp.data.token.TokenId
import com.masterproject.englishapp.exercises.SpeakTokenData
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.grammar.GClass
import com.masterproject.englishapp.recorder.AudioRecorder
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.utils.DummyAudioRecorder
import com.masterproject.englishapp.utils.extractSkillIds

@Composable
fun SpeakWordContent(data: SpeakTokenData, onResult: (ExerciseResult) -> Unit) {
    SpeakLayout(
        recorder = data.recorder,
        skillIds = data.learningWord.extractSkillIds(),
        targetText = data.learningWord,
        feedbackText = data.feedbackWord,
        instruction = "Say the words!",
        onResult = onResult
    ) { annotatedText ->
        TextImageAnswerOption(
            text = data.learningWord.replaceFirstChar { it.uppercase() },
            image = tokenImagePainter(data.tokenId),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            borderColor = AppColors.Gray400,
            textOnTop = true,
            imageTextSpacing = 24.dp,
            aspectRatio = 0.85f,
            onClick = { }
        )
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
    SpeakWordContent(
        data = SpeakTokenData(
            tokenId = TokenId(GClass.NOUN, Category.ANIMAL, "bear"),
            learningWord = "bear",
            feedbackWord = "urso",
            recorder = DummyAudioRecorder
        ),
        onResult = { }
    )
}