package com.masterproject.englishapp.components.scrollbars

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun CustomScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(AppColors.Gray200, shape = CircleShape)
    ) {
        val viewPortHeight = scrollState.viewportSize.toFloat()
        val totalHeight = scrollState.maxValue.toFloat() + viewPortHeight
        val indicatorHeight = (viewPortHeight / totalHeight) * viewPortHeight

        val scrollOffset = (scrollState.value.toFloat() / totalHeight) * viewPortHeight

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { indicatorHeight.toDp() })
                .graphicsLayer { translationY = scrollOffset }
                .background(AppColors.Primary, shape = CircleShape)
        )
    }
}