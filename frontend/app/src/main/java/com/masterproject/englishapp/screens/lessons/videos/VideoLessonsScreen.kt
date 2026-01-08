package com.masterproject.englishapp.screens.lessons.videos

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.VideoFloatingPlayer
import com.masterproject.englishapp.components.buttons.AnimatedAiButton
import com.masterproject.englishapp.components.loaders.OverlayLoader
import com.masterproject.englishapp.screens.lessons.videos.components.AiInputDialog
import com.masterproject.englishapp.screens.lessons.videos.components.TabHeaderItem
import com.masterproject.englishapp.ui.theme.AppColors
import java.io.File

data class Lesson(val title: String, val description: String, val youtubeId: String)
enum class VideoTab { YOUTUBE, GENERATED }

@Composable
fun VideoLessonsScreen(
    viewModel: VideoGeneratorViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    VideoLessonsScreenRouter(
        showInputPrompt = viewModel.showInputPrompt,
        isGenerating = viewModel.isGenerating,
        generatedVideoFile = viewModel.generatedVideoFile,
        onShowPromptChange = { viewModel.showInputPrompt = it },
        onCloseVideo = { viewModel.generatedVideoFile = null },
        onGenerateVideo = { idea -> viewModel.generateVideo(context, idea) },
        onVideoClick = { file -> viewModel.playVideo(file) }
    )
}

@Composable
fun VideoLessonsScreenRouter(
    showInputPrompt: Boolean,
    isGenerating: Boolean,
    generatedVideoFile: File?,
    onShowPromptChange: (Boolean) -> Unit,
    onCloseVideo: () -> Unit,
    onGenerateVideo: (String) -> Unit,
    onVideoClick: (File) -> Unit
) {
    var selectedTab by remember { mutableStateOf(VideoTab.YOUTUBE) }

    val indicatorOffset by animateFloatAsState(
        targetValue = if (selectedTab == VideoTab.YOUTUBE) 0f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "indicator"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                val tabWidth = maxWidth / 2

                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TabHeaderItem(
                            title = "YouTube",
                            isSelected = selectedTab == VideoTab.YOUTUBE,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = VideoTab.YOUTUBE }
                        )
                        TabHeaderItem(
                            title = "Meus Vídeos",
                            isSelected = selectedTab == VideoTab.GENERATED,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = VideoTab.GENERATED }
                        )
                    }

                    // Animated Bar
                    Box(
                        modifier = Modifier
                            .offset(x = tabWidth * indicatorOffset)
                            .width(tabWidth)
                            .height(3.dp)
                            .padding(horizontal = 20.dp)
                            .background(
                                color = AppColors.Primary,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    VideoTab.YOUTUBE -> {
                        VideoLessonsScreenContent()
                    }
                    VideoTab.GENERATED -> {
                        GeneratedVideosGalleryContent(
                            onVideoClick = onVideoClick
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            AnimatedAiButton(R.drawable.ic_plus_1) {
                onShowPromptChange(true)
            }
        }

        if (showInputPrompt) {
            AiInputDialog(
                onDismiss = { onShowPromptChange(false) },
                onGenerate = { idea -> onGenerateVideo(idea) }
            )
        }

        if (isGenerating) {
            OverlayLoader()
        }

        generatedVideoFile?.let { videoFile ->
            VideoFloatingPlayer(
                file = videoFile,
                onClose = { onCloseVideo() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoLessonsScreenPreview() {
    VideoLessonsScreenRouter(
        showInputPrompt = false,
        isGenerating = false,
        generatedVideoFile = null,
        onShowPromptChange = {},
        onCloseVideo = {},
        onGenerateVideo = {},
        onVideoClick = {}
    )
}