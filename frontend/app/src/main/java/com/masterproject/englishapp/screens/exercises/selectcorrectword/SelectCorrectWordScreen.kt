package com.masterproject.englishapp.screens.exercises.selectcorrectword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.masterproject.englishapp.exercises.SelectCorrectWordData
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.utils.extractSkillIds

@Composable
fun SelectCorrectWordContent(
    data: SelectCorrectWordData,
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
            Bubble(text = "Fill in the blank!", side = Side.Left, textColor = AppColors.Black800)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(resId = R.drawable.ic_two_people_talking_3, size = 300.dp)

            Spacer(modifier = Modifier.height(24.dp))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                data.words.forEachIndexed { index, word ->
                    if (index == data.gapIndex) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(80.dp)
                                .align(Alignment.Bottom),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .background(AppColors.Primary, shape = RoundedCornerShape(2.dp))
                            )

                            Text(
                                text = "",
                                fontSize = 19.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    } else {
                        Text(
                            text = word,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            color = AppColors.Black800,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(data.allOptionsShuffled) { word ->
                Rect3DCard(
                    contentPadding = 18.dp,
                    onClick = {
                        val translation = "Significado: ${data.feedbackPhrase.text}"

                        onResult(
                            ExerciseResult.fromBool(
                                skillIds = data.learningPhrase.extractSkillIds(),
                                isCorrect = word == data.correctOption,
                                correctMessage = translation,
                                wrongMessage = translation
                            )
                        )
                    }
                ) {
                    Text(
                        text = word,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
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
fun SelectCorrectWordScreenPreview() {
    val learningPhrase = Phrase(
        id = "1",
        text = "The quick brown fox jumps over the lazy dog",
        language = Language.EN,
        isQuestion = false
    )

    val feedbackPhrase = Phrase(
        id = "1",
        text = "O rápido raposo castanho salta sobre o cão preguiçoso",
        language = Language.PT,
        isQuestion = false
    )

    val words = learningPhrase.text.split(" ")

    val gapIndex = 3
    val correctOption = "fox"

    val previewData = SelectCorrectWordData(
        learningPhrase = learningPhrase,
        words = words,
        gapIndex = gapIndex,
        correctOption = correctOption,
        allOptionsShuffled = listOf("cat", "fox", "dog", "bird").shuffled(),
        feedbackPhrase = feedbackPhrase
    )

    SelectCorrectWordContent(
        data = previewData,
        onResult = { }
    )
}