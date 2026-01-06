package com.masterproject.englishapp.screens.intro.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.LanguageSelectionList
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.grammar.Language

@Composable
fun AskLearningLangScreen(
    selectedLanguage: Language?,
    onLanguageSelected: (Language) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AppIcon(resId = R.drawable.dragon_hello2, size = 60.dp)
            Spacer(Modifier.width(16.dp))
            Bubble(text = "What language do you want to learn?", side = Side.Left)
        }
        Spacer(Modifier.height(24.dp))

        LanguageSelectionList(
            languages = listOf(Language.EN),
            selectedLanguage = selectedLanguage,
            onLanguageSelected = onLanguageSelected
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
fun AskLearningScreenPreview() = AskLearningLangScreen(null) { }