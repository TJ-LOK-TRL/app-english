package com.masterproject.englishapp.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.vision.ImageClassification
import com.masterproject.englishapp.vision.ObjectRecognition
import com.masterproject.englishapp.vision.VisionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class OverlayShape {
    data class Rect(val x: Float, val y: Float, val width: Float, val height: Float, val color: Color = Color.Red) : OverlayShape()
    data class Circle(val x: Float, val y: Float, val radius: Float, val color: Color = Color.Blue) : OverlayShape()
}

class CameraViewModel(application: Application) :  AndroidViewModel(application) {

    val analysisIntervalMs: Long = 1500L

    private val classifier = ImageClassification(application)

    private val _overlays = MutableStateFlow<List<OverlayShape>>(emptyList())
    val overlays: StateFlow<List<OverlayShape>> get() = _overlays

    fun onFrameCaptured(bitmap: Bitmap) {
        viewModelScope.launch {
            val results: List<VisionResult> = classifier.analyzeFrame(bitmap)

            android.util.Log.d("CameraViewModel", "onFrameCaptured called, results size: ${results.size}")

            // Logs
            results.forEach { result ->
                android.util.Log.d(
                    "CameraViewModel",
                    "Detected: ${result.label} with confidence ${result.confidence}"
                )
            }

            val shapes: List<OverlayShape> = results.map { result ->
                OverlayShape.Circle(
                    x = 200f,
                    y = 300f,
                    radius = 50f,
                    color = Color.Blue
                )
            }

            _overlays.value = shapes
        }
    }

    override fun onCleared() {
        super.onCleared()
        classifier.close()
    }
}
