package com.masterproject.englishapp.screens.lessons.content

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.animations.ConfettiEffect
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun LessonEndScreen(
    onEndClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Bubble(
                        text = "Lessons completed!",
                        side = Side.Bottom,
                        textColor = AppColors.Black800,
                        fontSize = 21.sp
                    )
                    AppIcon(
                        resId = R.drawable.dragon_fire_1_nbg,
                        size = 256.dp,
                        flipHorizontal = true
                    )
                }
            }

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Continue",
                onClick = onEndClick
            )
        }

        ConfettiEffect()
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
fun LessonEndScreenPreview() = LessonEndScreen {
    Log.d("test", "ExerciseEndScreenPreview: click")
}