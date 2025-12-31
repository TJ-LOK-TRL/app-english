package com.masterproject.englishapp.components.cardspots

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CardSpots(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: CardColors = CardDefaults.cardColors(),
    spotPositions: List<SpotPosition> = emptyList(),
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            modifier = modifier,
            onClick = onClick,
            shape = shape,
            colors = colors
        ) {
            CardSpotsContent(spotPositions = spotPositions, content = content)
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = colors
        ) {
            CardSpotsContent(spotPositions = spotPositions, content = content)
        }
    }
}

@Composable
private fun CardSpotsContent(
    spotPositions: List<SpotPosition>,
    content: @Composable ColumnScope.() -> Unit
) {
    Box {
        // Draw all spots at specified positions
        spotPositions.forEach { spot ->
            Image(
                painter = painterResource(spot.imageId),
                contentDescription = null,
                modifier = Modifier
                    .size(spot.size)
                    .offset(x = spot.x, y = spot.y)
                    .alpha(spot.alpha),
                contentScale = ContentScale.Crop
            )
        }

        // Original card content
        Column(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}