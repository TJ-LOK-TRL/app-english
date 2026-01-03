package com.masterproject.englishapp.components

import android.graphics.BlurMaskFilter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side

sealed class OverlayShape {
    data class Rect(val x: Float, val y: Float, val width: Float, val height: Float, val color: Color = Color.Red) : OverlayShape()

    data class Circle(val x: Float, val y: Float, val radius: Float, val color: Color = Color.Blue) : OverlayShape()

    data class ARTag(
        val x: Float,
        val y: Float,
        val boxWidth: Float,
        val boxHeight: Float,
        val label: String,
        val confidence: Float,
        val isNear: Boolean,
        val onClick: () -> Unit
    ) : OverlayShape()

    data class ScannerRect(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val cornerLength: Float = 60f,
        val strokeW: Float = 6f,
        val color: Color = Color.Red,
        val animate: Boolean = false,
        val animationProgress: Float = 0f, // 0f to 1f
        val scanningLineColor: Color = Color.Green.copy(alpha = 0.7f)
    ) : OverlayShape()
}

@Composable
fun OverlayLayer(
    shapes: List<OverlayShape>,
    containerSize: IntSize
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ScannerTransition")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ScanProgress"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            shapes.forEach { shape ->
                when {
                    shape is OverlayShape.Rect -> drawRect(
                        color = shape.color,
                        topLeft = Offset(shape.x, shape.y),
                        size = Size(shape.width, shape.height),
                        alpha = 0.5f,
                        style = Stroke(width = 4f)
                    )

                    shape is OverlayShape.Circle -> drawCircle(
                        color = shape.color,
                        radius = shape.radius,
                        center = Offset(shape.x, shape.y),
                        alpha = 0.5f,
                        style = Stroke(width = 4f)
                    )

                    shape is OverlayShape.ScannerRect -> {
                        val currentProgress = if (shape.animate) progress else shape.animationProgress
                        val cornerLength = shape.cornerLength
                        val strokeW = shape.strokeW

                        // ---------- CANTO SUPERIOR ESQUERDO ----------
                        // Linha horizontal esquerda (superior)
                        drawLine(
                            color = shape.color,
                            start = Offset(shape.x, shape.y),
                            end = Offset(shape.x + cornerLength, shape.y),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )
                        // Linha vertical esquerda (superior)
                        drawLine(
                            color = shape.color,
                            start = Offset(shape.x, shape.y),
                            end = Offset(shape.x, shape.y + cornerLength),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )

                        // ---------- CANTO SUPERIOR DIREITO ----------
                        // Linha horizontal direita (superior)
                        drawLine(
                            color = shape.color,
                            start = Offset(shape.x + shape.width, shape.y),
                            end = Offset(shape.x + shape.width - cornerLength, shape.y),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )
                        // Linha vertical direita (superior)
                        drawLine(
                            color = shape.color,
                            start = Offset(shape.x + shape.width, shape.y),
                            end = Offset(shape.x + shape.width, shape.y + cornerLength),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )

                        // ---------- CANTO INFERIOR ESQUERDO ----------
                        // Linha horizontal esquerda (inferior)
                        drawLine(
                            color = shape.color,
                            start = Offset(shape.x, shape.y + shape.height),
                            end = Offset(shape.x + cornerLength, shape.y + shape.height),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )
                        // Linha vertical esquerda (inferior)
                        drawLine(
                            color = shape.color,
                            start = Offset(shape.x, shape.y + shape.height),
                            end = Offset(shape.x, shape.y + shape.height - cornerLength),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )

                        // ---------- CANTO INFERIOR DIREITO ----------
                        // Linha horizontal direita (inferior)
                        drawLine(
                            color = shape.color,
                            start = Offset(shape.x + shape.width, shape.y + shape.height),
                            end = Offset(shape.x + shape.width - cornerLength, shape.y + shape.height),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )
                        // Linha vertical direita (inferior)
                        drawLine(
                            color = shape.color,
                            start = Offset(shape.x + shape.width, shape.y + shape.height),
                            end = Offset(shape.x + shape.width, shape.y + shape.height - cornerLength),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )

                        // ---------- LINHA DE SCAN ANIMADA ----------
                        if (shape.animate) {
                            val scanY = shape.y + (shape.height * currentProgress)

                            // Linha de scan principal
                            drawLine(
                                color = shape.scanningLineColor,
                                start = Offset(shape.x, scanY),
                                end = Offset(shape.x + shape.width, scanY),
                                strokeWidth = strokeW * 0.8f,
                                cap = StrokeCap.Round
                            )

                            // Gradiente/sombra na linha de scan
                            drawLine(
                                color = shape.scanningLineColor.copy(alpha = 0.3f),
                                start = Offset(shape.x, scanY - 2),
                                end = Offset(shape.x + shape.width, scanY - 2),
                                strokeWidth = strokeW * 1.5f,
                                blendMode = BlendMode.Screen
                            )

                            // Pontos nas extremidades da linha de scan
                            drawCircle(
                                color = shape.scanningLineColor,
                                center = Offset(shape.x, scanY),
                                radius = strokeW
                            )
                            drawCircle(
                                color = shape.scanningLineColor,
                                center = Offset(shape.x + shape.width, scanY),
                                radius = strokeW
                            )
                        }
                    }
                }
            }
        }

        shapes.filterIsInstance<OverlayShape.ARTag>().forEach { tag ->
            ARTagComponent(tag, containerSize.width, containerSize.height)
        }
    }
}

@Composable
fun ARTagComponent(
    tag: OverlayShape.ARTag,
    containerWidth: Int,
    containerHeight: Int
) {
    if (containerWidth <= 0 || containerHeight <= 0) return

    val density = LocalDensity.current
    var tagSize by remember { mutableStateOf(IntSize.Zero) }
    val tipSize = 50f // Bubble tip size

    // Decide side (Top or Bottom)
    val side = remember(tag.y, tag.boxHeight, tagSize, containerHeight) {
        val th = tagSize.height.toFloat()
        if ((tag.y + tag.boxHeight + th + 40f) < containerHeight) Side.Top else Side.Bottom
    }

    // Calculate Horizontal Position
    // (x + half of height) - half height of tag
    val centerX = remember(tag.x, tag.boxWidth, tagSize, containerWidth) {
        val tw = tagSize.width.toFloat()
        val desiredX = (tag.x + (tag.boxWidth / 2)) - (tw / 2)
        desiredX.coerceIn(10f, (containerWidth - tw - 10f).coerceAtLeast(0f))
    }

    // Calculate Vertical Position
    val centerY = remember(tag.y, tag.boxHeight, tagSize, side) {
        val th = tagSize.height.toFloat()
        if (side == Side.Top) {
            tag.y + tag.boxHeight + tipSize
        } else {
            tag.y - th - tipSize
        }
    }
    Box(
        modifier = Modifier
            .offset(
                x = with(density) { centerX.toDp() },
                y = with(density) { centerY.toDp() }
            )
            .onGloballyPositioned { coords -> tagSize = coords.size }
            .animateContentSize()
            .clickable { tag.onClick() }
    ) {
        Bubble(
            text = tag.label.uppercase(),
            side = side,
            tipSize = tipSize,
            backgroundColor = if (tag.isNear) Color(0xFF1B5E20).copy(0.9f) else Color.Black.copy(0.8f),
            textColor = Color.White,
            borderWidth = 1.5.dp,
            borderColor = if (tag.isNear) Color.Green else Color.Cyan.copy(0.6f),
            padding = 10.dp,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (tag.isNear) 18.sp else 14.sp
        )
    }
}