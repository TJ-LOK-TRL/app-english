package com.masterproject.englishapp.screens.exercises.ordersentence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.MeasuredList
import com.masterproject.englishapp.components.animatePlacement
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.cards.WordCard
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.exercises.OrderSentenceData
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.ui.theme.AppColors
@Composable
fun OrderSentenceContent(
    data: OrderSentenceData,
    onResult: (ExerciseResult) -> Unit
) {
    var slots by remember {
        mutableStateOf(
            data.shuffledWords.mapIndexed { index, word ->
                WordSlot(word = word, id = index)
            }
        )
    }

    var selectedOrder by remember { mutableStateOf(listOf<Int>()) }

    val userWords = selectedOrder.mapNotNull { id -> slots.find { it.id == id }?.word }
    val isFull = userWords.size == slots.size
    val canCheck = userWords.isNotEmpty()

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
            Bubble(text = "Arrange the sentences correctly!", side = Side.Left, textColor = AppColors.Black800)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(resId = R.drawable.ic_two_people_talking, size = 180.dp)

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MeasuredList(
                    displayList = selectedOrder.mapNotNull { id -> slots.find { it.id == id } },
                    fullList = slots,
                    howCreate = { _, slot ->
                        key(slot.id) {
                            WordCard(
                                modifier = Modifier.animatePlacement(),
                                word = slot.word,
                                visible = true
                            ) {
                                slots = slots.mapIndexed { i, s ->
                                    if (i == slot.id) s.copy(isSelected = false) else s
                                }
                                selectedOrder = selectedOrder.filter { it != slot.id }
                            }
                        }
                    },
                ) { lineYs, totalHeight ->
                    Canvas(modifier = Modifier.fillMaxWidth().height(
                        with(LocalDensity.current) { totalHeight.toDp() }
                    )) {
                        lineYs.forEachIndexed { index, y ->
                            drawLine(
                                color = Color.LightGray,
                                start = Offset(0f, y.toFloat()),
                                end = Offset(size.width, y.toFloat())
                            )
                        }
                    }
                }
            }
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            slots.forEachIndexed { index, slot ->
                WordCard(
                    word = slot.word,
                    visible = !slot.isSelected
                ) {
                    slots = slots.mapIndexed { i, s ->
                        if (i == index) s.copy(isSelected = true) else s
                    }
                    selectedOrder = selectedOrder + slot.id
                }
            }
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Verificar",
            enabled = canCheck && isFull,
            onClick = {

                val userSentence = userWords.joinToString(" ")
                val correctSentence = data.correctOrder.joinToString(" ")
                val isCorrect = userWords == data.correctOrder

                android.util.Log.d("DEBUG_ORDER", "--- COMPARAÇÃO ---")
                android.util.Log.d("DEBUG_ORDER", "User:    '$userSentence'")
                android.util.Log.d("DEBUG_ORDER", "Correct: '$correctSentence'")
                android.util.Log.d("DEBUG_ORDER", "Resultado: $isCorrect")

                if (userWords.size != data.correctOrder.size) {
                    android.util.Log.d("DEBUG_ORDER", "Aviso: Tamanhos diferentes! User: ${userWords.size} vs Correct: ${data.correctOrder.size}")
                }

                onResult(ExerciseResult.fromBool(isCorrect))
            }
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
fun OrderSentenceScreenPreview() {
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

    val correctOrder = listOf(
        "I", "am", "a", "good", "human."
    )

    val shuffledWords = correctOrder.shuffled()

    val previewData = OrderSentenceData(
        learningPhrase = learningPhrase,
        shuffledWords = shuffledWords,
        correctOrder = correctOrder,
        feedbackPhrase = feedbackPhrase
    )

    OrderSentenceContent(
        data = previewData,
        onResult = { }
    )
}

data class WordSlot(
    val id: Int,
    val word: String,
    val isSelected: Boolean = false
)