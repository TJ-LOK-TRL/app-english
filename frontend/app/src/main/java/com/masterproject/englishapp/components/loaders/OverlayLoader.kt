package com.masterproject.englishapp.components.loaders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun OverlayLoader() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.3f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            androidx.compose.material3.CircularProgressIndicator(
                color = AppColors.Primary,
                strokeWidth = 4.dp
            )
        }
    }
}