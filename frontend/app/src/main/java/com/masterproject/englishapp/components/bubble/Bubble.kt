package com.masterproject.englishapp.components.bubble

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun Bubble(
    text: String,
    side: Side,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    borderWidth: Dp = 1.dp,
    borderColor: Color = AppColors.Gray300,
    textAlign: TextAlign = TextAlign.Center,
    textColor: Color = AppColors.Black700,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    padding: Dp = 12.dp,
    tipSize: Float = 20.0f
) {
    val shape = bubbleShape(side, tipSize = tipSize)

    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .border(
                BorderStroke(borderWidth, borderColor),
                shape = shape
            )
            .padding(padding)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor,
            textAlign = textAlign,
            modifier = textModifier,
        )
    }
}