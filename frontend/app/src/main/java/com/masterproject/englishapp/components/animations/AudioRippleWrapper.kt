package com.masterproject.englishapp.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AudioRippleWrapper(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Scale animation for pulse effect
    val infiniteTransition = rememberInfiniteTransition(label = "ripple")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isPlaying) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Glow animation (alpha)
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isPlaying) 0.7f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = if (isPlaying) scale else 1f
                scaleY = if (isPlaying) scale else 1f
                this.alpha = if (isPlaying) alpha else 1f
            }
    ) {
        content()
    }
}