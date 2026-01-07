package com.masterproject.englishapp.screens.chatbot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.audio.playPcmAudio
import com.masterproject.englishapp.chatbot.ChatMessage
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@HiltViewModel
class ChatBotViewModel @Inject constructor(
    private val apiService: ApiService,
    private val uiEventService: UiEventService
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending = _isSending.asStateFlow()

    private val _playingMessageId = MutableStateFlow<String?>(null)
    val playingMessageId = _playingMessageId.asStateFlow()

    init {
        // Init message
        if (_messages.value.isEmpty()) {
            _messages.value = listOf(
                ChatMessage(
                    text = "Hello! I'm your Dragon English tutor. How can I help you today?",
                    isUser = false
                )
            )
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // Add the message to the user UI
        val userMsg = ChatMessage(text = text, isUser = true)
        _messages.value = _messages.value + userMsg

        _isSending.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.chat(text)

                withContext(Dispatchers.Main) {
                    val botMsg = ChatMessage(
                        text = response.text,
                        isUser = false,
                        audioBase64 = response.audio
                    )
                    _messages.value = _messages.value + botMsg
                }
            } catch (e: Exception) {
                uiEventService.showError("Chat error: ${e.localizedMessage}")
            } finally {
                _isSending.value = false
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun playBotAudio(messageId: String, base64Audio: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _playingMessageId.value = messageId

                val audioData = Base64.decode(base64Audio)

                // Only RAW PCM
                val pcmData = audioData.copyOfRange(44, audioData.size)

                playPcmAudio(pcmData, 24000) // Kokoro is 24k
            } catch (e: Exception) {
                Log.e("ChatBot", "Error playing the audio", e)
                uiEventService.showError("Erro ao tocar áudio")
            } finally {
                _playingMessageId.value = null
            }
        }
    }
}