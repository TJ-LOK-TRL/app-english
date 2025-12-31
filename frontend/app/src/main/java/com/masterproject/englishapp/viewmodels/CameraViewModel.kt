package com.masterproject.englishapp.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.RectF
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.vision.ImageClassification
import com.masterproject.englishapp.vision.ImageUtils
import com.masterproject.englishapp.vision.ObjectRecognition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class OverlayShape {
    data class Rect(val x: Float, val y: Float, val width: Float, val height: Float, val color: Color = Color.Red) : OverlayShape()
    data class Circle(val x: Float, val y: Float, val radius: Float, val color: Color = Color.Blue) : OverlayShape()
}

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    val analysisIntervalMs: Long = 2500L
    
    private val classifier = ImageClassification(application)
    private val detector = ObjectRecognition(application)

    private var previewWidth = 1
    private var previewHeight = 1

    private val _overlays = MutableStateFlow<List<OverlayShape>>(emptyList())
    val overlays: StateFlow<List<OverlayShape>> get() = _overlays

    fun updatePreviewSize(width: Int, height: Int) {
        previewWidth = width
        previewHeight = height
        android.util.Log.d("CameraViewModel", "Preview size updated: ${width}x${height}")
    }

    fun onFrameCaptured(bitmap: Bitmap) {
        viewModelScope.launch {
            // SCALE FACTORS (convert bitmap to preview coordinates)
            val scaleX = previewWidth / bitmap.width.toFloat()
            val scaleY = previewHeight / bitmap.height.toFloat()


            // Detect objects
            val detections = detector.analyzeFrame(bitmap)
            android.util.Log.d("CameraViewModel", "Objects detected: ${detections.size}")

            val results = mutableListOf<DetectedObject>()

            // For each object detected
            for (d in detections) {
                android.util.Log.d("CameraViewModel", "Detection: boundingBox=${d.boundingBox}")

                // Crop area of the bitmap corresponding to the detected area
                val cropped = ImageUtils.cropBitmap(bitmap, d.boundingBox)
                android.util.Log.d("CameraViewModel", "Cropped bitmap: width=${cropped.width}, height=${cropped.height}")
                ImageUtils.saveBitmapToGallery(getApplication(), cropped, "cropped_${System.currentTimeMillis()}")

                // Classify only the cropped area
                val classification = classifier.analyzeFrame(cropped)
                val label = classification?.label ?: "unknown"
                val confidence = classification?.confidence ?: 0f

                android.util.Log.d(
                    "CameraViewModel",
                    "Classification: label=$label, confidence=$confidence"
                )

                results.add(
                    DetectedObject(
                        boundingBox = d.boundingBox,
                        label = label,
                        classificationConfidence = confidence
                    )
                )
            }

            // Create overlays
            val shapes = results.map { res ->
                val box = res.boundingBox

                val scaledLeft = box.left * scaleX
                val scaledTop = box.top * scaleY
                val scaledWidth = box.width() * scaleX
                val scaledHeight = box.height() * scaleY

                OverlayShape.Rect(
                    x = scaledLeft,
                    y = scaledTop,
                    width = scaledWidth,
                    height = scaledHeight,
                    color = Color.Red
                )

                //OverlayShape.Rect(
                //    x = box.left,
                //    y = box.top,
                //    width = box.width(),
                //    height = box.height(),
                //    color = Color.Red
                //)
            }

            _overlays.value = shapes
            android.util.Log.d("CameraViewModel", "Overlays created: ${shapes.size}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        classifier.close()
    }
}

data class DetectedObject(
    val boundingBox: RectF,
    val label: String,
    val classificationConfidence: Float
)
