package com.masterproject.englishapp.components

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.masterproject.englishapp.vision.ImageUtils

@Composable
fun CameraPreview(
    flashEnabled: Boolean = false,
    onFrame: (Bitmap) -> Unit,
    onPreviewSizeChanged: (width: Int, height: Int) -> Unit
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    var camera by remember { mutableStateOf<androidx.camera.core.Camera?>(null) }

    LaunchedEffect(flashEnabled) {
        camera?.cameraControl?.enableTorch(flashEnabled)
    }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)

            val preview = Preview.Builder().build()
            preview.surfaceProvider = previewView.surfaceProvider

            val selector = CameraSelector.DEFAULT_BACK_CAMERA

            val analysis = androidx.camera.core.ImageAnalysis.Builder()
                .setBackpressureStrategy(androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { image ->
                val bitmap = image.toBitmap()

                val rotatedBitmap = if (image.imageInfo.rotationDegrees != 0) {
                    ImageUtils.rotateBitmap(bitmap, image.imageInfo.rotationDegrees.toFloat())
                } else {
                    bitmap
                }

                onFrame(rotatedBitmap)
                image.close()
            }

            try {
                val cameraProvider = ProcessCameraProvider.getInstance(ctx).get()
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, analysis)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            previewView
        },
        modifier = Modifier.onGloballyPositioned { layout ->
            val w = layout.size.width
            val h = layout.size.height
            onPreviewSizeChanged(w, h)
        }
    )
}