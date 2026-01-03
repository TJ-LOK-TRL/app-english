package com.masterproject.englishapp.components.headers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun ProgressHeader(progress: Float, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            AppIcon(resId = R.drawable.arrow_back, size = 20.dp, tint = AppColors.Gray700)
        }

        Spacer(modifier = Modifier.width(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(8.dp),
            color = AppColors.Primary,
            trackColor = AppColors.Gray300,
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp,
            drawStopIndicator = { }
        )
    }
}