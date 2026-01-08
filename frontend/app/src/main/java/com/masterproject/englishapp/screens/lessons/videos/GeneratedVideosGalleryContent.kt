package com.masterproject.englishapp.screens.lessons.videos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import java.io.File

@Composable
fun GeneratedVideosGalleryContent(
    onVideoClick: (File) -> Unit
) {
    val context = LocalContext.current
    val videos = remember {
        val directory = File(context.cacheDir, "videos")
        directory.listFiles { file -> file.extension == "mp4" }
            ?.sortedByDescending { it.lastModified() } ?: emptyList<File>()
    }

    if (videos.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Ainda não geraste nenhum vídeo.")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            items(videos) { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onVideoClick(video) },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        AppIcon(resId = R.drawable.ic_play, size = 24.dp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = video.nameWithoutExtension, fontWeight = FontWeight.SemiBold)
                            Text(text = "Tamanho: ${video.length() / 1024 / 1024} MB", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}