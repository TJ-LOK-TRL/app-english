package com.masterproject.englishapp.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    resId: Int,
    size: Dp = 384.dp,
    flipHorizontal: Boolean = false,
    tint: Color = Color.Unspecified
) {
    Icon(
        painter = painterResource(resId),
        contentDescription = null,
        modifier = modifier
            .size(size)
            .graphicsLayer {
                if (flipHorizontal) scaleX = -1f
            },
        tint = tint,
    )
}