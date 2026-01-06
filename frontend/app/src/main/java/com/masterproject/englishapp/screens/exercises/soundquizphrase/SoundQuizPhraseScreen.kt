package com.masterproject.englishapp.screens.exercises.soundquizphrase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.R
import com.masterproject.englishapp.audio.playPcmAudio
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.Circular3DButton
import com.masterproject.englishapp.components.cards.Rect3DCard
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.exercises.SoundQuizPhraseData
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun SoundQuizPhraseContent(
    data: SoundQuizPhraseData,
    onResult: (ExerciseResult) -> Unit
) {
    val allOptions = remember {
        (data.wrongPhrases + data.correctPhrase).shuffled()
    }

    val optionPhrases = remember {
        allOptions.associateWith { phrase ->
            phrase.text.replaceFirstChar { it.uppercase() }
        }
    }

    var isSpeaking by remember { mutableStateOf(false) }

    LaunchedEffect(isSpeaking) {
        if (!isSpeaking) return@LaunchedEffect

        playPcmAudio(
            audioData = data.audio,
            sampleRate = data.sampleRate
        )

        isSpeaking = false
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
            Bubble(text = "Listen carefully!", side = Side.Left, textColor = AppColors.Black800)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Circular3DButton(
                    size = 72.dp,
                    color = AppColors.Primary,
                    depth = 3.dp,
                    contentOffsetX = 2.dp,
                    onClick = {
                        if (!isSpeaking) {
                            isSpeaking = true
                        }
                    }
                ) {
                    AppIcon(
                        resId = if (isSpeaking) R.drawable.ic_speaker_on else R.drawable.ic_play,
                        size = 32.dp,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Text(
                    text = "What does the audio say?",
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Black800,
                    fontSize = 22.sp
                )

                Text(
                    text = "Can't listen now",
                    fontSize = 16.sp,
                    color = AppColors.Gray600,
                    modifier = Modifier.clickable {
                        onResult(ExerciseResult.Skipped())
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            allOptions.forEach { phrase ->
                Rect3DCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = 16.dp,
                    onClick = {
                        onResult(
                            ExerciseResult.fromBool(
                                skillIds = listOf(data.correctPhrase.id),
                                isCorrect = phrase.id == data.correctPhrase.id,
                                correctMessage = null,
                                wrongMessage = null
                            )
                        )
                    }
                ) {
                    Text(
                        text = phrase.text,
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
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
fun SoundQuizPhraseScreenPreview() {
    val correctPhrase = Phrase(
        id = "phrase_1",
        text = "The weather is beautiful today.",
        language = Language.EN,
        isQuestion = false
    )

    val wrongPhrases = listOf(
        Phrase(
            id = "phrase_2",
            text = "I would like to order a coffee.",
            language = Language.EN,
            isQuestion = false
        ),
        Phrase(
            id = "phrase_3",
            text = "Where is the nearest train station?",
            language = Language.EN,
            isQuestion = true
        ),
        Phrase(
            id = "phrase_4",
            text = "I have been studying English for two years.",
            language = Language.EN,
            isQuestion = false
        )
    )

    val previewData = SoundQuizPhraseData(
        audio = ByteArray(0),
        sampleRate = 44100,
        correctPhrase = correctPhrase,
        wrongPhrases = wrongPhrases
    )

    SoundQuizPhraseContent(
        data = previewData,
        onResult = { }
    )
}