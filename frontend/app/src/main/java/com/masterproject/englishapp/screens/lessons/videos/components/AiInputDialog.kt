package com.masterproject.englishapp.screens.lessons.videos.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun AiInputDialog(onDismiss: () -> Unit, onGenerate: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("O que vamos aprender hoje?") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Ex: Dicas de aeroporto em Inglês") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onGenerate(text) }) { Text("Gerar Vídeo") }
        }
    )
}
