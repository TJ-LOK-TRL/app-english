package com.masterproject.englishapp.components.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.ui.theme.AppColors
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChart(
    stats: Map<String, Float>,
    modifier: Modifier = Modifier,
    color: Color = AppColors.Primary
) {
    val labelList = stats.keys.toList()
    val values = stats.values.toList()
    val numPoints = 6
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(
        color = AppColors.Gray600,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
    )

    Canvas(modifier = modifier.fillMaxWidth().height(260.dp).padding(40.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2
        val angleStep = 2 * PI / numPoints

        // Draw background web (3 levels)
        for (i in 1..3) {
            val currentRadius = radius * (i / 3f)
            val path = Path()
            for (j in 0 until numPoints) {
                val angle = j * angleStep - PI / 2
                val x = center.x + currentRadius * cos(angle).toFloat()
                val y = center.y + currentRadius * sin(angle).toFloat()
                if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, Color.LightGray.copy(alpha = 0.5f), style = Stroke(width = 1.dp.toPx()))
        }

        // Draw axis lines
        for (j in 0 until numPoints) {
            val angle = j * angleStep - PI / 2
            val cosAngle = cos(angle).toFloat()
            val sinAngle = sin(angle).toFloat()

            val x = center.x + radius * cosAngle
            val y = center.y + radius * sinAngle
            drawLine(Color.LightGray.copy(alpha = 0.5f), center, Offset(x, y), strokeWidth = 1.dp.toPx())

            // Draw Labels
            val label = labelList.getOrNull(j) ?: ""
            val percentage = ((values.getOrNull(j) ?: 0f) * 100).toInt()

            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AppColors.Gray800
                )
                ) {
                    append("$percentage%\n")
                }
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = AppColors.Gray500
                )) {
                    append(label)
                }
            }

            val measuredText = textMeasurer.measure(
                text = annotatedText,
                style = TextStyle(textAlign = TextAlign.Center)
            )

            val horizontalOffset = abs(cosAngle) * (measuredText.size.width / 2f)
            val verticalOffset = abs(sinAngle) * (measuredText.size.height / 2f)

            val extraMargin = 15.dp.toPx()

            val textX = center.x + (radius + extraMargin + horizontalOffset) * cosAngle - (measuredText.size.width / 2)
            val textY = center.y + (radius + extraMargin + verticalOffset) * sinAngle - (measuredText.size.height / 2)

            drawText(
                textLayoutResult = measuredText,
                topLeft = Offset(textX, textY)
            )
        }

        // Draw filled area (Actual progress)
        val progressPath = Path()
        val pointPositions = mutableListOf<Offset>()
        for (j in 0 until numPoints) {
            val angle = j * angleStep - PI / 2
            val value = values.getOrElse(j) { 0f }
            val x = center.x + (radius * value) * cos(angle).toFloat()
            val y = center.y + (radius * value) * sin(angle).toFloat()

            val position = Offset(x, y)
            pointPositions.add(position)

            if (j == 0) progressPath.moveTo(x, y) else progressPath.lineTo(x, y)
        }
        progressPath.close()
        drawPath(progressPath, color.copy(alpha = 0.3f))
        drawPath(progressPath, color, style = Stroke(width = 2.dp.toPx()))

        // Draw data points (circles at each vertex)
        pointPositions.forEach { position ->
            // White circle (background)
            drawCircle(
                color = Color.White,
                radius = 5.dp.toPx(),
                center = position
            )
            // Circle border (primary color)
            drawCircle(
                color = color,
                radius = 5.dp.toPx(),
                center = position,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}