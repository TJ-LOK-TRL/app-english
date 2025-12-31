package com.masterproject.englishapp.screens.intro.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.ui.theme.AppColors

@Preview(
    name = "Xiaomi Redmi 9C",
    device = "spec:width=360dp,height=800dp,dpi=269",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEEEEEE
)
@Composable
fun QuestionsStarter() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Column(
            Modifier
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-20).dp)
        ) {
            Bubble(
                "Before the lesson, let's start with some questions to personalize your learning experience!",
                Side.Bottom,
                fontSize = 24.sp,
                textColor = AppColors.Black700,
            )

            Icon(
                painter = painterResource(R.drawable.dragon_point_up_small),
                contentDescription = null,
                Modifier.size(384.dp),
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.weight(1.0f))

        PrimaryButton(
            "Continue",
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}