package com.masterproject.englishapp.screens.lessons.videos

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VideoGeneratorViewModel @Inject constructor(
    private val apiService: ApiService,
    private val uiEventService: UiEventService
) : ViewModel() {

    var isGenerating by mutableStateOf(false)
    var generatedVideoFile by mutableStateOf<File?>(null)
    var showInputPrompt by mutableStateOf(false)

    fun generateVideo(context: Context, userInput: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isGenerating = true
            showInputPrompt = false
            try {
                val safeName = userInput.take(20).replace(Regex("[^a-zA-Z0-9]"), "_")
                val timestamp = System.currentTimeMillis()

                val response = apiService.generateVideoContent(userInput)
                val cacheVideos = File(context.cacheDir, "videos")
                if (!cacheVideos.exists()) cacheVideos.mkdirs()

                val file = File(cacheVideos, "${safeName}_$timestamp.mp4")

                response.byteStream().use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                generatedVideoFile = file
            } catch (e: Exception) {
                uiEventService.showError(e.localizedMessage ?: "Erro ao gerar vídeo")
            } finally {
                isGenerating = false
            }
        }
    }

    fun playVideo(file: File) {
        generatedVideoFile = file
    }
}