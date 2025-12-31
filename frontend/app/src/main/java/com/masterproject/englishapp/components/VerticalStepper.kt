package com.masterproject.englishapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun VerticalStepper(
    index: Int? = null,
    circleSize: Dp = 18.dp,
    lineTopOffset: Dp = 12.dp
) {
    Box(
        modifier = Modifier
            .width(24.dp)
            .fillMaxHeight()
    ) {

        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .padding(top = circleSize + lineTopOffset)
                .background(Color.LightGray)
                .align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .size(circleSize)
                .background(AppColors.Primary, CircleShape)
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            if (index != null) {
                Text(
                    text = index.toString(),
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}