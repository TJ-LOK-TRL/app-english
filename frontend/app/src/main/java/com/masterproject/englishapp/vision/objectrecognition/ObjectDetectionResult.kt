package com.masterproject.englishapp.vision.objectrecognition

import android.graphics.RectF

data class ObjectDetectionResult(
    val label: String,
    val confidence: Float,
    val boundingBox: RectF
)