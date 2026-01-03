package com.masterproject.englishapp.vision

import android.graphics.RectF

data class DetectedObject(
    val boundingBox: RectF,
    val label: String,
    val classificationConfidence: Float
)