    package com.masterproject.englishapp.screens

    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.padding
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.masterproject.englishapp.components.CameraPreview
    import com.masterproject.englishapp.components.OverlayLayer
    import com.masterproject.englishapp.viewmodels.CameraViewModel

    @Composable
    fun CameraScreen(
        cameraViewModel: CameraViewModel = viewModel()
    ) {
        var lastAnalysisTime by remember { mutableLongStateOf(0L) }

        val overlays by cameraViewModel.overlays.collectAsState()
    
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview { bitmap ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastAnalysisTime >= cameraViewModel.analysisIntervalMs) {
                    lastAnalysisTime = currentTime
                    cameraViewModel.onFrameCaptured(bitmap)
                }
            }

            OverlayLayer(shapes = overlays)

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                // Buttons later maybe
            }
        }
    }