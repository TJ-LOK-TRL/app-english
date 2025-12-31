package com.masterproject.englishapp.components.cardspots

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SpotPosition(
    val imageId: Int,
    val x: Dp,
    val y: Dp,
    val alpha: Float = 0.1f,
    val size: Dp = 60.dp
)