package com.masterproject.englishapp.vision.imageclassification

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.Classifications

class ImageClassification(context: Context) {
    private var classifier: ImageClassifier? = null

    init {
        try {
            val options = ImageClassifier.ImageClassifierOptions.builder()
                .setMaxResults(1)
                .build()

            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "models/efficientnet-lite4.tflite",
                options
            )
        } catch (e: Exception) {
            Log.e("ObjectRecognition", "Error initializing classifier: ${e.message}")
        }
    }

    fun analyzeFrame(bitmap: Bitmap): ImageClassificationResult? {
        return classifier?.let { classifier ->
            try {
                val image = TensorImage.fromBitmap(bitmap)
                val results: List<Classifications> = classifier.classify(image)

                results.firstOrNull()
                    ?.categories
                    ?.maxByOrNull { it.score }
                    ?.let { category ->
                        ImageClassificationResult(
                            label = category.label,
                            confidence = category.score
                        )
                    }
            } catch (e: Exception) {
                Log.e("ObjectRecognition", "Error analyzing frame: ${e.message}")
                null
            }
        }
    }

    fun close() {
        classifier?.close()
    }
}