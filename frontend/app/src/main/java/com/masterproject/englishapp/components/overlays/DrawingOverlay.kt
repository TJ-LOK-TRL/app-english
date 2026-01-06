package com.masterproject.englishapp.components.overlays

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun DrawingOverlay(
    modifier: Modifier = Modifier,
    onDrawFinished: (List<Offset>) -> Unit
) {
    val points = remember { mutableStateListOf<Offset>() }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        points.clear()
                        points.add(offset)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        points.add(change.position)
                    },
                    onDragEnd = {
                        if (points.size > 10) {
                            val first = points.first()
                            val last = points.last()
                            val distance = sqrt(
                                (last.x - first.x).toDouble().pow(2.0) +
                                        (last.y - first.y).toDouble().pow(2.0)
                            )

                            if (distance < 100f) {
                                onDrawFinished(points.toList())
                            }
                        }
                        points.clear()
                    }
                )
            }
    ) {
        if (points.size > 1) {
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(points.first().x, points.first().y)
                points.forEach { lineTo(it.x, it.y) }
            }
            drawPath(
                path = path,
                color = Color.Cyan,
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )
        }
    }
}