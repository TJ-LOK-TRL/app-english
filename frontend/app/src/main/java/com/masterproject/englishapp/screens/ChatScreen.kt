// screens/ChatScreen.kt
package com.masterproject.englishapp.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ChatScreen() {
    val inputText = remember { mutableStateOf("") }
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // WebView
        Card(Modifier.fillMaxWidth().weight(1f).padding(bottom = 16.dp)) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        loadUrl("file:///android_asset/web/avatar3D/index.html")
                        webViewRef.value = this
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Controls
        Column(Modifier.fillMaxWidth()) {
            TextField(
                value = inputText.value,
                onValueChange = { inputText.value = it },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                placeholder = { Text("Type text for avatar...") },
                singleLine = true
            )

            Button(
                onClick = {
                    if (inputText.value.isNotBlank()) {
                        val js = """
                            document.getElementById('text').value = '${inputText.value}';
                            document.getElementById('speak').click();
                        """.trimIndent()
                        webViewRef.value?.evaluateJavascript(js, null)
                        inputText.value = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Speak")
            }
        }
    }
}