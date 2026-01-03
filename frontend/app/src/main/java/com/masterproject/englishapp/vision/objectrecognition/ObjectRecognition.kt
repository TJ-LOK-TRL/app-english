package com.masterproject.englishapp.vision.objectrecognition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import org.tensorflow.lite.task.vision.detector.Detection

class ObjectRecognition(context: Context) {
    private var detector: ObjectDetector? = null

    init {
        try {
            val options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(5)
                .setScoreThreshold(0.3f)
                .build()

            detector = ObjectDetector.createFromFileAndOptions(
                context,
                "models/efficientdet-lite0.tflite",
                options
            )
        } catch (e: Exception) {
            Log.e("ObjectRecognition", "Error initializing detector: ${e.message}")
        }
    }

    fun analyzeFrame(bitmap: Bitmap): List<ObjectDetectionResult> {
        return detector?.let { det ->
            try {
                val tensorImage = TensorImage.fromBitmap(bitmap)

                val results: List<Detection> = det.detect(tensorImage)

                results.map { detection ->
                    val label = detection.categories.firstOrNull()?.label ?: "Unknown"
                    val confidence = detection.categories.firstOrNull()?.score ?: 0f
                    val box: RectF = detection.boundingBox

                    ObjectDetectionResult(
                        label = label,
                        confidence = confidence,
                        boundingBox = box
                    )
                }
            } catch (e: Exception) {
                Log.e("ObjectRecognition", "Error analyzing frame: ${e.message}")
                emptyList()
            }
        } ?: emptyList()
    }

    fun close() {
        detector?.close()
    }
}