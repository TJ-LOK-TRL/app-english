package com.masterproject.englishapp.chatbot

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val audioBase64: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)