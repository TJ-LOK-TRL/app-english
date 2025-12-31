package com.masterproject.englishapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MatchExerciseScreen(
    englishWords: List<String> = listOf("I", "am", "waiting"),
    portugueseWords: List<String> = listOf("Eu", "estou", "à espera"),
    onExerciseFinished: (Boolean) -> Unit
) {
    // Shuffle both lists only once
    val shuffledEnglish = remember { englishWords.shuffled() }
    val shuffledPortuguese = remember { portugueseWords.shuffled() }

    // State for selections and matched pairs
    var selectedEnglish by remember { mutableStateOf<String?>(null) }
    var selectedPortuguese by remember { mutableStateOf<String?>(null) }
    val matchedPairs by remember { mutableStateOf(mutableSetOf<Pair<String, String>>()) }

    var errorPair by remember { mutableStateOf<Pair<String, String>?>(null) }

    // Checks if selected items match based on original index
    fun handleSelection() {
        if (selectedEnglish != null && selectedPortuguese != null) {
            if (englishWords.indexOf(selectedEnglish) == portugueseWords.indexOf(selectedPortuguese)) {
                matchedPairs.add(selectedEnglish!! to selectedPortuguese!!)
                // Check if exercise is finished
                if (matchedPairs.size == englishWords.size) {
                    onExerciseFinished(true)
                }
            } else {
                // Trigger error animation
                errorPair = selectedEnglish!! to selectedPortuguese!!
            }
            selectedEnglish = null
            selectedPortuguese = null
        }
    }

    // Side effect to reset error after short delay
    LaunchedEffect(errorPair) {
        if (errorPair != null) {
            kotlinx.coroutines.delay(500)
            errorPair = null
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // English column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "English",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            shuffledEnglish.forEach { word ->
                val isMatched = matchedPairs.any { it.first == word }
                WordBox(
                    text = word,
                    enabled = !isMatched,
                    selected = selectedEnglish == word,
                    showError = errorPair?.first == word,
                    onClick = {
                        selectedEnglish = word
                        handleSelection()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        // Portuguese column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Português",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            shuffledPortuguese.forEach { word ->
                val isMatched = matchedPairs.any { it.second == word }
                WordBox(
                    text = word,
                    enabled = !isMatched,
                    selected = selectedPortuguese == word,
                    showError = errorPair?.second == word,
                    onClick = {
                        selectedPortuguese = word
                        handleSelection()
                    }
                )
            }
        }
    }
}

@Composable
fun WordBox(text: String, enabled: Boolean, selected: Boolean, showError: Boolean, onClick: () -> Unit) {
    val borderColor = when {
        showError -> Color.Red
        !enabled -> Color.Green
        selected -> Color.Yellow
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}

