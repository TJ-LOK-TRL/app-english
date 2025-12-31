package com.masterproject.englishapp.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Circular3DButton(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    color: Color = Color(0xFF1E88E5),
    depth: Dp = 3.dp,
    contentOffsetX: Dp = 0.dp,
    contentOffsetY: Dp = 0.dp,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val bottomColor = color.copy(red = color.red * 0.7f, green = color.green * 0.7f, blue = color.blue * 0.7f)

    Box(
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bottomColor, shape = CircleShape)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = depth)
                .graphicsLayer {
                    translationY = -depth.toPx() / 2
                }
                .background(color, shape = CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.offset(x = contentOffsetX, y = contentOffsetY),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}
