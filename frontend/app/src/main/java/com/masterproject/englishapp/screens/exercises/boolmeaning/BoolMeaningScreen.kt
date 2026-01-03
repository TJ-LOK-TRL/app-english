package com.masterproject.englishapp.screens.exercises.boolmeaning

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.cards.Rect3DCard
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.utils.extractSkillIds
import com.masterproject.englishapp.exercises.BoolMeaningData
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun BoolMeaningContent(
    data: BoolMeaningData,
    onResult: (ExerciseResult) -> Unit
) {
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
            Bubble(text = "Choose true or false!", side = Side.Left, textColor = AppColors.Black800)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(resId = R.drawable.ic_two_people_talking_3, size = 310.dp)

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = data.learningPhrase.text,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary,
                    fontSize = 22.sp
                )

                Text(
                    text = "significa:",
                    fontSize = 16.sp,
                    color = AppColors.Gray600,
                )

                Text(
                    text = data.feedbackPhrase.text,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Black800,
                    fontSize = 22.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Rect3DCard(
                modifier = Modifier.weight(1f),
                contentPadding = 18.dp,
                onClick = {
                    onResult(
                        ExerciseResult.fromBool(
                            skillIds = data.learningPhrase.extractSkillIds(),
                            isCorrect = data.isCorrectAnswer,
                            correctMessage = "Significado: ${data.feedbackPhrase.text}",
                            wrongMessage = "Significado: ${data.feedbackPhrase.text}"
                        )
                    )
                }
            ) {
                Text(
                    text = "Verdadeiro",
                    color = AppColors.Black700,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
            Rect3DCard(
                modifier = Modifier.weight(1f),
                contentPadding = 18.dp,
                onClick = {
                    onResult(
                        ExerciseResult.fromBool(
                            skillIds = data.learningPhrase.extractSkillIds(),
                            isCorrect = !data.isCorrectAnswer,
                            correctMessage = "Significado: ${data.feedbackPhrase.text}",
                            wrongMessage = "Significado: ${data.feedbackPhrase.text}"
                        )
                    )
                }
            ) {
                Text(
                    text = "Falso",
                    color = AppColors.Black700,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
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
    val learningPhrase = Phrase(
        id = "I am a human.",
        text = "I am a human.",
        language = Language.EN,
        isQuestion = false
    )

    val feedbackPhrase = Phrase(
        id = "I am a human.",
        text = "Eu sou humano.",
        language = Language.PT,
        isQuestion = false
    )

    val previewData = BoolMeaningData(
        learningPhrase = learningPhrase,
        feedbackPhrase = feedbackPhrase,
        isCorrectAnswer = true
    )

    BoolMeaningContent(previewData) { }
}