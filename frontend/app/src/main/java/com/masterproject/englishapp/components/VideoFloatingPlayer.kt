package com.masterproject.englishapp.components

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.io.File
import com.masterproject.englishapp.R

@OptIn(UnstableApi::class)
@Composable
fun VideoFloatingPlayer(
    file: File,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.fromFile(file))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true // Starts playing immediately when opened
        }
    }

    // Ensure the player is released when the component leaves the screen
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Floating design (manual PIP style)
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center // You can change to BottomCenter if preferred
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f), // Maintains the format of your AI-generated videos
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Integration of classic Android PlayerView in Compose
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true // Shows play/pause/progress bar
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Close button in the top corner
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_1),
                        contentDescription = "Close Video",
                        tint = Color.White
                    )
                }
            }
        }
    }
}