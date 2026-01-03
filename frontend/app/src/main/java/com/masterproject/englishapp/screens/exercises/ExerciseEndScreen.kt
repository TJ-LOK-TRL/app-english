package com.masterproject.englishapp.screens.exercises

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.animations.ConfettiEffect
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.cards.MetricCard
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun ExerciseEndScreen(
    timeElapsed: String,
    accuracy: Int,
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

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Time Card
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Time",
                        headerColor = Color(0xFF2196F3)
                    ) {
                        MetricContent(R.drawable.ic_timer, timeElapsed)
                    }

                    // Accuracy Card
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Accuracy",
                        headerColor = AppColors.Primary
                    ) {
                        MetricContent(R.drawable.ic_target, "$accuracy%")
                    }
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

@Composable
private fun MetricContent(iconRes: Int, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AppIcon(resId = iconRes, size = 28.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.Black800
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
fun ExerciseEndScreenPreview() = ExerciseEndScreen("2:25", 92) {
    Log.d("test", "ExerciseEndScreenPreview: click")
}