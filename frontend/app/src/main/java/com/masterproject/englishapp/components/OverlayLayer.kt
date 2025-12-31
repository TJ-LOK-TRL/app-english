package com.masterproject.englishapp.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import com.masterproject.englishapp.viewmodels.OverlayShape

@Composable
fun OverlayLayer(shapes: List<OverlayShape>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        shapes.forEach { shape ->
            when (shape) {
                is OverlayShape.Rect -> drawRect(
                    color = shape.color,
                    topLeft = Offset(shape.x, shape.y),
                    size = Size(shape.width, shape.height),
                    alpha = 0.5f,
                    style = Stroke(width = 4f)
                )
                is OverlayShape.Circle -> drawCircle(
                    color = shape.color,
                    radius = shape.radius,
                    center = Offset(shape.x, shape.y),
                    alpha = 0.5f,
                    style = Stroke(width = 4f)
                )
            }
        }
    }
}
