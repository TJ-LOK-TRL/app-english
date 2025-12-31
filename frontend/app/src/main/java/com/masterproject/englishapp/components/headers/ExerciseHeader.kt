package com.masterproject.englishapp.components.headers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.R

@Composable
fun ExerciseHeader(
    currentStep: Int,
    totalSteps: Int,
    onCloseClick: () -> Unit,
    onMoreClick: () -> Unit,
    trackBarColor: Color,
    trackBackgroundColor: Color,
) {
    val progressTarget = if (totalSteps > 0) currentStep.toFloat() / totalSteps else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 500),
        label = "ProgressAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = "Sair",
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )
        }

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .weight(1f)
                .height(15.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = trackBarColor,
            trackColor = trackBackgroundColor,
            strokeCap = StrokeCap.Square,
            gapSize = 0.dp,
            drawStopIndicator = { }
        )

        IconButton(
            onClick = onMoreClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = "Mais opções",
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}