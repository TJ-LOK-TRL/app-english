package com.masterproject.englishapp.components.buttons

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.components.AppIcon

@Composable
fun AnimatedAiButton(
    resId: Int,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "offset"
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFF007A), Color(0xFF00C2FF), Color(0xFF8F00FF), Color(0xFFFF7A00)),
        start = Offset(offset, offset),
        end = Offset(offset + 500f, offset + 500f),
        tileMode = TileMode.Mirror
    )

    CircularAi3DButton(
        brush = gradient,
        size = 70.dp,
        onClick = onClick,
        depth = 0.dp
    ) {
        AppIcon(resId = resId, size = 32.dp, tint = Color.White)
    }
}