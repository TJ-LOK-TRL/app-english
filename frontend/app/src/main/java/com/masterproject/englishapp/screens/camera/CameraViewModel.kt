package com.masterproject.englishapp.screens.camera

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.components.overlays.OverlayShape
import com.masterproject.englishapp.network.ApiService
import com.masterproject.englishapp.vision.imageclassification.ImageClassification
import com.masterproject.englishapp.vision.ImageUtils
import com.masterproject.englishapp.vision.objectrecognition.ObjectRecognition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.masterproject.englishapp.network.safeApiCall
import com.masterproject.englishapp.result.getOrNull
import com.masterproject.englishapp.vision.DetectedObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@HiltViewModel
class CameraViewModel @Inject constructor(
    application: Application,
    private val api: ApiService
) : AndroidViewModel(application) {

    private val _debugInfo = MutableStateFlow<DebugResult?>(null)
    val debugInfo: StateFlow<DebugResult?> = _debugInfo

    val analysisIntervalMs: Long = 2000L
    
    private val classifier = ImageClassification(application)
    private val detector = ObjectRecognition(application)

    private var previewWidth = 1
    private var previewHeight = 1

    private val _autoOverlays = MutableStateFlow<List<OverlayShape>>(emptyList())

    private val _manualOverlays = MutableStateFlow<List<OverlayShape>>(emptyList())

    private val _audioToPlay = MutableStateFlow<ByteArray?>(null)
    val audioToPlay: StateFlow<ByteArray?> = _audioToPlay

    val overlays: StateFlow<List<OverlayShape>> = combine(_autoOverlays, _manualOverlays) { auto, manual ->
        auto + manual
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onFrameCaptured(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.Default) {
            // SCALE FACTORS (convert bitmap to preview coordinates)
            val scaleX = previewWidth / bitmap.width.toFloat()
            val scaleY = previewHeight / bitmap.height.toFloat()

            // Detect objects
            val detections = detector.analyzeFrame(bitmap)
            Log.d("CameraViewModel", "Objects detected: ${detections.size}")

            val results = mutableListOf<DetectedObject>()

            // For each object detected
            for (d in detections) {
                Log.d("CameraViewModel", "Detection: boundingBox=${d.boundingBox}")

                // Crop area of the bitmap corresponding to the detected area
                val cropped = ImageUtils.cropBitmap(bitmap, d.boundingBox)
                Log.d("CameraViewModel", "Cropped bitmap: width=${cropped.width}, height=${cropped.height}")
                //ImageUtils.saveBitmapToGallery(getApplication(), cropped, "cropped_${System.currentTimeMillis()}")

                // Classify only the cropped area
                val classification = classifier.analyzeFrame(cropped)
                val label = classification?.label ?: "unknown"
                val confidence = classification?.confidence ?: 0f

                Log.d(
                    "CameraViewModel",
                    "Classification: label=$label, confidence=$confidence"
                )

                results.add(
                    DetectedObject(
                        boundingBox = d.boundingBox,
                        label = d.label,
                        classificationConfidence = confidence
                    )
                )
            }

            // Create overlays
            val shapes = buildList {
                results.forEach { res ->
                    val box = res.boundingBox
                    val scaledLeft = box.left * scaleX
                    val scaledTop = box.top * scaleY
                    val scaledWidth = box.width() * scaleX
                    val scaledHeight = box.height() * scaleY
                    val objectArea = box.width() * box.height()
                    val totalArea = bitmap.width * bitmap.height
                    val isNear = (objectArea / totalArea.toFloat()) > 0.15f

                    add(
                        OverlayShape.ScannerRect(
                            x = scaledLeft,
                            y = scaledTop,
                            width = scaledWidth,
                            height = scaledHeight,
                            color = Color.Red
                        )
                    )

                    add(
                        OverlayShape.ARTag(
                            x = scaledLeft,
                            y = scaledTop,
                            boxWidth = scaledWidth,
                            boxHeight = scaledHeight,
                            label = res.label,
                            confidence = res.classificationConfidence,
                            isNear = isNear,
                            onClick = { speakLabel(res.label) }
                        )
                    )
                }
            }

            val fullClassification = classifier.analyzeFrame(bitmap)
            _debugInfo.value = DebugResult(
                fullImageLabel = fullClassification?.label ?: "None",
                crops = results.map { res ->
                    ImageUtils.cropBitmap(bitmap, res.boundingBox) to res
                }
            )

            _autoOverlays.value = shapes
            Log.d("CameraViewModel", "Overlays created: ${shapes.size}")
        }
    }

    fun onManualCrop(bitmap: Bitmap, points: List<Offset>, onFinished: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                if (points.isEmpty()) return@launch

                // Calculate Bounding Box of points
                val minX = points.minOf { it.x }
                val maxX = points.maxOf { it.x }
                val minY = points.minOf { it.y }
                val maxY = points.maxOf { it.y }

                // Convert coordinates of screen to Bitmap coordinates
                val scaleX = bitmap.width / previewWidth.toFloat()
                val scaleY = bitmap.height / previewHeight.toFloat()

                val rectInBitmap = android.graphics.Rect(
                    (minX * scaleX).toInt().coerceIn(0, bitmap.width),
                    (minY * scaleY).toInt().coerceIn(0, bitmap.height),
                    (maxX * scaleX).toInt().coerceIn(0, bitmap.width),
                    (maxY * scaleY).toInt().coerceIn(0, bitmap.height)
                )

                // Crop and classify
                val cropped = ImageUtils.cropBitmap(bitmap, rectInBitmap)
                val manualDetections = detector.analyzeFrame(cropped)
                val bestDetection = manualDetections.maxByOrNull { it.confidence }

                val label = bestDetection?.label ?: "Unknown"
                val confidence = bestDetection?.confidence ?: 0f

                // Create Overlay of the result
                val newManualTag = OverlayShape.ARTag(
                    x = minX,
                    y = minY,
                    boxWidth = maxX - minX,
                    boxHeight = maxY - minY,
                    label = label,
                    confidence = confidence,
                    isNear = true,
                    onClick = { speakLabel(label) }
                )

                val newManualCircle = OverlayShape.ManualCircle(points = points)

                // Update overlays
                _manualOverlays.value = _manualOverlays.value + listOf(newManualTag, newManualCircle)

                // TTS
                speakLabel(label)
            } finally {
                withContext(Dispatchers.Main) {
                    onFinished()
                }
            }
        }
    }
    @OptIn(ExperimentalEncodingApi::class)
    fun speakLabel(label: String) {
        viewModelScope.launch {
            val synthResult = safeApiCall { api.synthesize(label) }.getOrNull()
            Log.d("CameraViewModel", "synthResult: $synthResult")
            synthResult?.let {
                val audioBytes = Base64.decode(it.audio)
                _audioToPlay.value = audioBytes
                // it.sampleRate // TODO: use this sampleRate please
            }
        }
    }

    fun updatePreviewSize(width: Int, height: Int) {
        previewWidth = width
        previewHeight = height
        Log.d("CameraViewModel", "Preview size updated: ${width}x${height}")
    }

    fun clearAudio() { _audioToPlay.value = null }

    fun clearAllOverlays() {
        _manualOverlays.value = emptyList()
        _autoOverlays.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        classifier.close()
    }
}

data class DebugResult(
    val fullImageLabel: String,
    val crops: List<Pair<Bitmap, DetectedObject>>
)