package com.masterproject.englishapp.screens.camera

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.components.OverlayShape
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

    val analysisIntervalMs: Long = 2500L
    
    private val classifier = ImageClassification(application)
    private val detector = ObjectRecognition(application)

    private var previewWidth = 1
    private var previewHeight = 1

    private val _overlays = MutableStateFlow<List<OverlayShape>>(emptyList())
    val overlays: StateFlow<List<OverlayShape>> get() = _overlays

    private val _audioToPlay = MutableStateFlow<ByteArray?>(null)
    val audioToPlay: StateFlow<ByteArray?> = _audioToPlay

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
                        label = label,
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

                    add(
                        OverlayShape.ScannerRect(
                            x = scaledLeft,
                            y = scaledTop,
                            width = scaledWidth,
                            height = scaledHeight,
                            color = Color.Red
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

            _overlays.value = shapes
            Log.d("CameraViewModel", "Overlays created: ${shapes.size}")
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

    fun clearAudio() { _audioToPlay.value = null }

    fun updatePreviewSize(width: Int, height: Int) {
        previewWidth = width
        previewHeight = height
        Log.d("CameraViewModel", "Preview size updated: ${width}x${height}")
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