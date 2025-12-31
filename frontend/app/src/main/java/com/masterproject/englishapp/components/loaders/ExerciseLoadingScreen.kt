package com.masterproject.englishapp.components.loaders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun ExerciseLoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppIcon(
            resId = R.drawable.dragon_point_up_small,
            size = 120.dp,
            modifier = Modifier.alpha(0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CircularProgressIndicator(
            color = AppColors.Primary,
            strokeWidth = 4.dp,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Loading next challenge...",
            color = AppColors.Gray600,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}