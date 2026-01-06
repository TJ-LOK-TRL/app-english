package com.masterproject.englishapp.screens.lessons.content.meaninglesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.lessons.content.meaninglesson.MeaningLessonData
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.utils.DummyMeaningLessonData

@Composable
fun MeaningLessonContent(data: MeaningLessonData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, end = 16.dp, top = 0.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIcon(
                resId = R.drawable.dragon_point_up_small,
                size = 96.dp,
                flipHorizontal = true
            )
            Bubble(text = data.contextTitle, side = Side.Left, textColor = AppColors.Black800)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(resId = R.drawable.ic_two_people_talking_3, size = 310.dp)

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = data.learningPhrase,
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
                    text = data.feedbackPhrase,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Black800,
                    fontSize = 22.sp
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
fun MeaningLessonPreview() = MeaningLessonContent(DummyMeaningLessonData)