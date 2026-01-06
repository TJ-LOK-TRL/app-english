package com.masterproject.englishapp.screens.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.overlays.OverlayLayer

@Composable
fun ModelDebugScreen(viewModel: CameraViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val debugInfo by viewModel.debugInfo.collectAsState()
    val overlays by viewModel.overlays.collectAsState()
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val testImages = remember {
        listOf(R.drawable.img_dog_1, R.drawable.img_banana_1, R.drawable.img_woman_yoga, R.drawable.img_child_girl_1)
    }
    var currentImageIndex by remember { mutableIntStateOf(0) }

    val bitmap = remember(currentImageIndex) {
        BitmapFactory.decodeResource(context.resources, testImages[currentImageIndex])
            .copy(Bitmap.Config.ARGB_8888, true)
    }

    val bitmapContainerSize = remember(bitmap) {
        IntSize(bitmap.width, bitmap.height)
    }

    LaunchedEffect(bitmap) {
        viewModel.updatePreviewSize(bitmap.width, bitmap.height)
        viewModel.onFrameCaptured(bitmap)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .onGloballyPositioned { coordinates ->
                containerSize = coordinates.size
            }
    ) {
        Text("AR Model Lab (Pixel-Perfect Mode)", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = { currentImageIndex = (currentImageIndex + 1) % testImages.size }) {
            Text("Próxima Imagem")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 1. IMAGEM ORIGINAL COM CLASSIFICAÇÃO GLOBAL ---
        Text("1. Original & Global Label", fontWeight = FontWeight.Bold)
        Card(modifier = Modifier.padding(vertical = 8.dp)) {
            Box {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.wrapContentSize(), // Mantém o tamanho original
                    contentScale = ContentScale.None
                )
                Text(
                    text = "Global: ${debugInfo?.fullImageLabel}",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(Color.Black.copy(0.7f))
                        .padding(8.dp),
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. DETECÇÃO COM SCROLL HORIZONTAL (Se a imagem for larga) ---
        Text("2. Detection & Overlays (Scroll Horizontal se necessário)", fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()) // Permite ver imagens largas
                .background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier.size(
                    width = with(LocalDensity.current) { bitmap.width.toDp() },
                    height = with(LocalDensity.current) { bitmap.height.toDp() }
                )
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.None
                )
                // O Overlay agora deve bater certo pois o container tem o tamanho exato do bitmap
                OverlayLayer(shapes = overlays, containerSize = bitmapContainerSize)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. CROPS REAIS ENVIADOS AO CLASSIFICADOR ---
        Text("3. Model's Eye (Crops)", fontWeight = FontWeight.Bold)

        debugInfo?.crops?.forEach { (cropBitmap, detection) ->
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Mostra o Crop com Aspect Ratio Original
                        Image(
                            bitmap = cropBitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .height(120.dp) // Altura fixa, largura ajusta pelo aspect
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Fit
                        )

                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text("Classified as: ${detection.label}", fontWeight = FontWeight.Bold, color = Color.Blue)
                            Text("Conf: ${(detection.classificationConfidence * 100).toInt()}%")
                        }
                    }
                }
            }
        }
    }
}
