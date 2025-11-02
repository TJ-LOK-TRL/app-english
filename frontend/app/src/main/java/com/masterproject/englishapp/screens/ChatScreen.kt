// screens/ChatScreen.kt
package com.masterproject.englishapp.screens

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
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
import androidx.webkit.WebViewAssetLoader

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ChatScreen() {
    val inputText = remember { mutableStateOf("") }
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    // BOX permite sobreposição (WebView no fundo, controles por cima)
    androidx.compose.foundation.layout.Box(Modifier.fillMaxSize()) {

        // === WebView (fundo) ===
        AndroidView(
            factory = { context ->
                val assetLoader = WebViewAssetLoader.Builder()
                    .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
                    .build()

                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {
                            request?.url?.let { return assetLoader.shouldInterceptRequest(it) }
                            return null
                        }
                    }

                    webChromeClient = object : android.webkit.WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: android.webkit.ConsoleMessage): Boolean {
                            android.util.Log.d(
                                "WebViewJS",
                                "${consoleMessage.message()} (Line ${consoleMessage.lineNumber()})"
                            )
                            return true
                        }
                    }

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    settings.allowUniversalAccessFromFileURLs = true
                    settings.allowFileAccessFromFileURLs = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                    loadUrl("https://appassets.androidplatform.net/assets/web/avatar3D/index.html")

                    webViewRef.value = this
                }
            },
            modifier = Modifier.fillMaxSize() // ocupa a tela inteira
        )

        // === Controles (sobrepostos) ===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(androidx.compose.ui.Alignment.BottomCenter)
        ) {
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


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ChatScreen2() {
    val inputText = remember { mutableStateOf("") }
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    Column(Modifier.fillMaxSize()) {
        // WebView
        Card(Modifier.fillMaxWidth().weight(1f)) {
            AndroidView(
                factory = { context ->
                    val assetLoader = WebViewAssetLoader.Builder()
                        .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
                        .build()

                    WebView(context).apply {
                        webViewClient = object : WebViewClient() {
                            override fun shouldInterceptRequest(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): WebResourceResponse? {
                                request?.url?.let { return assetLoader.shouldInterceptRequest(it) }
                                return null
                            }
                        }
                        webChromeClient = object : android.webkit.WebChromeClient() {
                            override fun onConsoleMessage(consoleMessage: android.webkit.ConsoleMessage): Boolean {
                                android.util.Log.d(
                                    "WebViewJS",
                                    "${consoleMessage.message()} (Line ${consoleMessage.lineNumber()})"
                                )
                                return true
                            }
                        }

                        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowFileAccess = true
                        settings.allowContentAccess = true
                        settings.allowUniversalAccessFromFileURLs = true
                        settings.allowFileAccessFromFileURLs = true
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        loadUrl("https://appassets.androidplatform.net/assets/web/avatar3D/index.html")

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