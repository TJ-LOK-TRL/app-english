package com.masterproject.englishapp.screens.exercises.soundquiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.optiongrid.TextImageAnswerOption
import com.masterproject.englishapp.components.tokenImagePainter
import com.masterproject.englishapp.data.Category
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.data.token.Token
import com.masterproject.englishapp.data.token.TokenId
import com.masterproject.englishapp.exercises.SoundQuizData
import com.masterproject.englishapp.grammar.GClass
import com.masterproject.englishapp.grammar.GNumber
import com.masterproject.englishapp.grammar.Gender
import com.masterproject.englishapp.grammar.NounValue
import com.masterproject.englishapp.R
import com.masterproject.englishapp.audio.playPcmAudio
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.Circular3DButton
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.network.safeApiCall

@Composable
fun SoundQuizContent(
    data: SoundQuizData,
    onResult: (ExerciseResult) -> Unit
) {
    val allOptions = remember {
        (data.wrongTokens + data.correctToken).shuffled()
    }

    val optionTexts = remember {
        allOptions.associateWith { token ->
            token.values.random().text.replaceFirstChar { it.uppercase() }
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
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppIcon(
                    resId = R.drawable.dragon_point_up_small,
                    size = 96.dp,
                    flipHorizontal = true
                )
                Bubble(text = "Choose the correct image!", side = Side.Left, textColor = AppColors.Black800)
            }

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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(allOptions) { token ->
                TextImageAnswerOption(
                    text = optionTexts[token]!!,
                    image = tokenImagePainter(token),
                    aspectRatio = 0.95f,
                    fontSize = 16.sp,
                    textColor = AppColors.Black800,
                    fontWeight = FontWeight.SemiBold,
                    borderColor = AppColors.Gray400,
                    borderWidth = 1.dp,
                    imageTextSpacing = 20.dp
                ) {
                    onResult(
                        ExerciseResult.fromBool(
                            skillIds = listOf(data.correctToken.id.toString()),
                            isCorrect = token == data.correctToken,
                            correctMessage = null,
                            wrongMessage = null
                        )
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
fun SoundQuizScreenPreview() {
    val correct = Token(
        id = TokenId(grammarClass = GClass.NOUN, category = Category.ANIMAL, localId = "elephant"),
        grammarClass = GClass.NOUN,
        language = Language.EN,
        values = listOf(NounValue("elephant", Gender.NEUTRAL, GNumber.NEUTRAL))
    )

    val wrong = listOf(
        Token(
            id = TokenId(GClass.NOUN, Category.ANIMAL, "dolphin"),
            grammarClass = GClass.NOUN,
            language = Language.EN,
            values = listOf(NounValue("dolphin", Gender.NEUTRAL, GNumber.NEUTRAL))
        ),
        Token(
            id = TokenId(GClass.NOUN, Category.ANIMAL, "bear"),
            grammarClass = GClass.NOUN,
            language = Language.EN,
            values = listOf(NounValue("bear", Gender.NEUTRAL, GNumber.NEUTRAL))
        ),
        Token(
            id = TokenId(GClass.NOUN, Category.ANIMAL, "camel"),
            grammarClass = GClass.NOUN,
            language = Language.EN,
            values = listOf(NounValue("camel", Gender.NEUTRAL, GNumber.NEUTRAL))
        )
    )

    // SoundQuizData fake
    val previewData = SoundQuizData(
        audio = ByteArray(0),
        sampleRate = 0,
        correctToken = correct,
        wrongTokens = wrong
    )



    SoundQuizContent(previewData) { }
}