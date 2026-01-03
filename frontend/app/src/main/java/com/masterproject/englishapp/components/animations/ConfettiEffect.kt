package com.masterproject.englishapp.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ConfettiEffect2() {
    val duration = 3000
    val particleCount = 50
    val colors = listOf(Color.Red, Color.Blue, Color.Yellow, Color.Green, Color.Magenta, Color.Cyan)

    val transitionState = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) { transitionState.targetState = true }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeight = constraints.maxHeight.toFloat()
        val screenWidth = constraints.maxWidth.toFloat()

        if (transitionState.targetState) {
            repeat(particleCount) { index ->
                val startX = remember { (0..screenWidth.toInt()).random().toFloat() }
                val color = remember { colors.random() }
                val size = remember { (8..16).random().dp }
                val delay = remember { (0..2000).random() }

                val animY = animateFloatAsState(
                    targetValue = screenHeight + 100f,
                    animationSpec = tween(
                        durationMillis = duration,
                        delayMillis = delay,
                        easing = LinearEasing
                    ),
                    label = "y"
                )

                // Simulate a rotation and horizontal balance
                val animRotation = animateFloatAsState(
                    targetValue = 360f * 3,
                    animationSpec = tween(duration, delay, LinearEasing),
                    label = "rotation"
                )

                Box(
                    modifier = Modifier
                        .offset(x = (startX / 2.5f).dp, y = animY.value.dp) // Offset Y animated
                        .rotate(animRotation.value)
                        .size(width = size / 2, height = size)
                        .background(color, RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
fun ConfettiEffect(modifier: Modifier = Modifier) {
    val particleCount = 40
    val colors = listOf(Color.Red, Color.Blue, Color.Yellow, Color.Green, Color.Magenta, Color.Cyan)
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val height = constraints.maxHeight.toFloat()
        val width = constraints.maxWidth.toFloat()

        repeat(particleCount) {
            // Valores aleatórios para cada fita
            val startX = remember { (0..width.toInt()).random().toFloat() }
            val color = remember { colors.random() }
            val sizeWidth = remember { (4..8).random().dp }
            val sizeHeight = remember { (10..18).random().dp }
            val delay = remember { (0..2000).random() }
            val duration = remember { (2500..4000).random() }

            // 1. Animação de Queda (Y) - Começa em -50 (fora do topo)
            val animY = remember { androidx.compose.animation.core.Animatable(-50f) }

            // 2. Animação de Rotação
            val animRotation = remember { androidx.compose.animation.core.Animatable(0f) }

            LaunchedEffect(Unit) {
                // Pequeno delay aleatório para não caírem todos juntos
                kotlinx.coroutines.delay(delay.toLong())

                // Lançar as duas animações em paralelo
                launch {
                    animY.animateTo(
                        targetValue = height,
                        animationSpec = tween(duration, easing = LinearEasing)
                    )
                }

                launch {
                    animRotation.animateTo(
                        targetValue = 1080f,
                        animationSpec = tween(duration, easing = LinearEasing)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset(
                        x = with(density) { startX.toDp() },
                        y = with(density) { animY.value.toDp() }
                    )
                    .rotate(animRotation.value)
                    .size(width = sizeWidth, height = sizeHeight)
                    .background(color, RoundedCornerShape(2.dp))
            )
        }
    }
}