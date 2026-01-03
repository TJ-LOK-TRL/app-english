package com.masterproject.englishapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.cards.Rect3DCard
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.ui.theme.AppColors

data class LanguageViewData(val language: Language, val resId: Int, val value: String)

@Composable
fun LanguageSelectionList(
    languages: List<Language>,
    selectedLanguage: Language?,
    onLanguageSelected: (Language) -> Unit
) {
    val languagesData = remember(languages) {
        languages.map { language ->
            when (language) {
                Language.EN -> LanguageViewData(language, R.drawable.flag_united_states, "English")
                Language.PT -> LanguageViewData(language, R.drawable.flag_portugal, "Português")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        languagesData.forEach { item ->
            val isSelected = item.language == selectedLanguage

            Rect3DCard(
                modifier = Modifier.fillMaxWidth(),
                depth = 2.dp,
                depthColor = if (isSelected) AppColors.Primary else null,
                borderColor = if (isSelected) AppColors.Primary else AppColors.Gray300,
                borderWidth = if (isSelected) 2.dp else 1.dp,
                onClick = { onLanguageSelected(item.language) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    AppIcon(resId = item.resId, size = 32.dp)

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = item.value,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = AppColors.Black800
                    )

                    if (isSelected) {
                        AppIcon(
                            resId = R.drawable.ic_checkmark_medium,
                            size = 32.dp,
                            tint = AppColors.Primary
                        )
                    }
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
fun LanguageSelectionListPreview() = LanguageSelectionList(Language.entries, Language.EN) { }
