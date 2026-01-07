package com.masterproject.englishapp.screens.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.audio.playPcmAudio
import com.masterproject.englishapp.components.CameraPreview
import com.masterproject.englishapp.components.overlays.OverlayLayer
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.overlays.OverlayShape
import com.masterproject.englishapp.components.buttons.Circular3DButton
import com.masterproject.englishapp.components.overlays.DrawingOverlay
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val overlays by cameraViewModel.overlays.collectAsState()
    val audioData by cameraViewModel.audioToPlay.collectAsState()
    val isAnalyzing by cameraViewModel.isAnalyzing.collectAsState()

    LaunchedEffect(audioData) {
        audioData?.let { bytes ->
            playPcmAudio(bytes, 24000)
            cameraViewModel.clearAudio()
        }
    }

    CameraScreenContent(
        overlays = overlays,
        isAnalyzing = isAnalyzing,
        onBack = onBack,
        onManualCrop = { bitmap, points, onFinished -> cameraViewModel.onManualCrop(bitmap, points, onFinished) },
        onFrameCaptured = { cameraViewModel.onFrameCaptured(it) },
        onPreviewSizeChanged = { w, h -> cameraViewModel.updatePreviewSize(w, h) },
        onClearOverlays = { cameraViewModel.clearAllOverlays() }
    )
}

@Composable
fun CameraScreenContent(
    overlays: List<OverlayShape> = emptyList(),
    isAnalyzing: Boolean = false,
    onBack: () -> Unit = {},
    onManualCrop: (Bitmap, List<Offset>, () -> Unit) -> Unit = { _, _, _ -> },
    onFrameCaptured: (Bitmap) -> Unit = {},
    onPreviewSizeChanged: (Int, Int) -> Unit = { _, _ -> },
    onClearOverlays: () -> Unit = {}
) {
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var isDrawingMode by remember { mutableStateOf(false) }
    var currentBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var flashEnabled by remember { mutableStateOf(false) }
    var isProcessingManual by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 65.dp, bottom = 128.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(width = 1.dp, color = AppColors.Primary, shape = RoundedCornerShape(16.dp))
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size
                }
        ) {
            CameraPreview(
                flashEnabled = flashEnabled,
                onFrame = { bitmap ->
                    if (!isDrawingMode && !isAnalyzing) {
                        currentBitmap = bitmap
                        onFrameCaptured(bitmap)
                    }
                },
                onPreviewSizeChanged = onPreviewSizeChanged
            )

            Box(modifier = Modifier.padding(12.dp).align(Alignment.TopEnd)) {
                Text(
                    text = if (isDrawingMode) "MODE: MANUAL DRAW" else "MODE: AUTO SCAN",
                    color = if (isDrawingMode) Color.Yellow else Color.Green,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.background(Color.Black.copy(0.5f), RoundedCornerShape(4.dp)).padding(4.dp)
                )
            }

            if (!isDrawingMode) {
                OverlayLayer(shapes = overlays, containerSize = containerSize)
            } else {
                DrawingOverlay { points ->
                    if (isProcessingManual) return@DrawingOverlay
                    isProcessingManual = true
                    currentBitmap?.let { bitmap ->
                        onManualCrop(bitmap, points) {
                            isDrawingMode = false
                            isProcessingManual = false
                        }
                    }
                    Log.d("DRAW", "Círculo fechado com ${points.size} pontos!")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .align(Alignment.TopStart)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(36.dp)
                ) {
                    AppIcon(resId = R.drawable.ic_arrow_left, tint = Color.White)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { flashEnabled = !flashEnabled },
                        modifier = Modifier.size(36.dp)
                    ) {
                        AppIcon(
                            resId = if (flashEnabled) R.drawable.ic_lightbulb_on else R.drawable.ic_light_off,
                            tint = if (flashEnabled) Color.Yellow else Color.White
                        )
                    }

                    IconButton(
                        onClick = onClearOverlays,
                        modifier = Modifier.size(26.dp),
                        shape = CircleShape
                    ) {
                        AppIcon(resId = R.drawable.ic_trash, tint = Color.White)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {
            Circular3DButton(
                onClick = { isDrawingMode = !isDrawingMode },
                color = AppColors.Primary,
                size = 70.dp
            ) {
                AppIcon(resId = R.drawable.ic_finger_draw3, tint = Color.White, size = 48.dp)
            }
        }

        if (isProcessingManual) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                androidx.compose.material3.CircularProgressIndicator(color = Color.Yellow)
            }
        }
    }
}

@Preview(
    name = "Xiaomi Redmi 9C",
    device = "spec:width=360dp,height=800dp,dpi=269",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEEEEEE
)
@Composable
fun CameraScreenPreview() {
    CameraScreenContent(
        overlays = emptyList(),
        onBack = {},
    )
}