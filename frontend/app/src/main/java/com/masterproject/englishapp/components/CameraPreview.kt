package com.masterproject.englishapp.components

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun CameraPreview(
    onFrame: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx)

        val preview = Preview.Builder().build()
        preview.surfaceProvider = previewView.surfaceProvider

        val selector = CameraSelector.DEFAULT_BACK_CAMERA

        val analysis = androidx.camera.core.ImageAnalysis.Builder()
            .setBackpressureStrategy(androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        analysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { image ->
            val bitmap = image.toBitmap()
            onFrame(bitmap)
            image.close()
        }

        ProcessCameraProvider.getInstance(ctx).get().apply {
            unbindAll()
            bindToLifecycle(lifecycleOwner, selector, preview, analysis)
        }

        previewView
    })
}