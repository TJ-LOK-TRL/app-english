package com.masterproject.englishapp.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

@Composable
fun Rect3DCard2(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp),
    color: Color = Color.White,
    depth: Dp = 4.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color(0xFFE0E0E0),
    onClick: () -> Unit,
    contentPadding: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val bottomColor = color.copy(
        red = color.red * 0.85f,
        green = color.green * 0.85f,
        blue = color.blue * 0.85f
    )

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(top = depth),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(bottomColor, shape)
        )

        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationY = -depth.toPx()
                }
                //.fillMaxWidth()
                .background(color, shape)
                .border(borderWidth, borderColor, shape)
                .padding(vertical = contentPadding),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

@Composable
fun Rect3DCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp),
    color: Color = Color.White,
    depth: Dp = 3.dp,
    depthColor: Color? = null,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color(0xFFE0E0E0),
    onClick: () -> Unit,
    contentPadding: Dp = 0.dp,
    faceVisible: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val bottomColor = depthColor ?: color.copy(
        red = color.red * 0.85f,
        green = color.green * 0.85f,
        blue = color.blue * 0.85f
    )

    Layout(
        modifier = modifier.clickable(onClick = onClick),
        content = {

            Box(
                modifier = Modifier.background(bottomColor, shape)
            )

            Box(
                modifier = Modifier
                    .background(color, shape)
                    .border(borderWidth, borderColor, shape)
                    .padding(contentPadding),
                contentAlignment = Alignment.Center,
                content = content
            )
        }
    ) { measurables, constraints ->
        val depthPx = depth.roundToPx()

        val facePlaceable = measurables[1].measure(constraints)

        val width = facePlaceable.width
        val height = facePlaceable.height + depthPx

        val bottom = measurables[0].measure(
            Constraints.fixed(width, facePlaceable.height)
        )

        layout(width, height) {
            bottom.place(0, depthPx)

            if (faceVisible) {
                facePlaceable.place(0, 0)
            }
        }
    }
}