package com.masterproject.englishapp.screens.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.R
import com.masterproject.englishapp.chatbot.ChatMessage
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.animations.AudioRippleWrapper
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun ChatBotScreen(
    viewModel: ChatBotViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isSending by viewModel.isSending.collectAsState()
    val playingMessageId by viewModel.playingMessageId.collectAsState()

    ChatBotContent(
        messages = messages,
        isSending = isSending,
        playingMessageId = playingMessageId,
        onSendMessage = { text -> viewModel.sendMessage(text) },
        onPlayAudio = { message ->
            message.audioBase64?.let { base64 ->
                viewModel.playBotAudio(message.id, base64)
            }
        }
    )
}

@Composable
fun ChatBotContent(
    messages: List<ChatMessage>,
    isSending: Boolean,
    playingMessageId: String?,
    onSendMessage: (String) -> Unit,
    onPlayAudio: (ChatMessage) -> Unit
) {
    var textState by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Scroll
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
    ) {
        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                ChatBubble(
                    message = message,
                    playingMessageId = playingMessageId,
                    onBubbleClick = { onPlayAudio(message) }
                )
            }
            if (isSending) {
                item {
                    Text(
                        "O bot está a escrever...",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    placeholder = { Text("Mensagem", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Send button
            Surface(
                shape = CircleShape,
                color = AppColors.Primary,
                modifier = Modifier.size(48.dp),
                onClick = {
                    if (textState.isNotBlank()) {
                        onSendMessage(textState)
                        textState = ""
                    }
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AppIcon(resId = R.drawable.ic_send_2, tint = Color.White, size = 20.dp)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    playingMessageId: String?,
    onBubbleClick: () -> Unit
) {
    val isThisPlaying = playingMessageId == message.id
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isUser) Color(0xFFFFCF83) else Color.White
    val shape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = if (message.isUser) 12.dp else 0.dp,
        bottomEnd = if (message.isUser) 0.dp else 12.dp
    )

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        AudioRippleWrapper(isPlaying = isThisPlaying) {
            Surface(
                color = bubbleColor,
                shape = shape,
                shadowElevation = 1.dp,
                modifier = Modifier.widthIn(max = 280.dp),
                onClick = { if (!message.isUser) onBubbleClick() }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = message.text, color = Color.Black, fontSize = 16.sp)

                    if (!message.isUser) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AppIcon(resId = R.drawable.ic_noise, tint = AppColors.Primary, size = 18.dp)
                    }
                }
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
fun ChatBotPreview() {
    val msg = ChatMessage(text = "Hello! How can I help you today?", isUser = false)

    val mockMessages = listOf(
        msg,
        ChatMessage(text = "I want to practice my English!", isUser = true),
        ChatMessage(text = "That's great! Let's start with some basic phrases.", isUser = false)
    )

    ChatBotContent(
        messages = mockMessages,
        isSending = false,
        playingMessageId = msg.id,
        onSendMessage = {},
        {}
    )
}