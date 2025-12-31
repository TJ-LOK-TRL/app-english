package com.masterproject.englishapp.components.cards

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    word: String,
    visible: Boolean,
    onClick: () -> Unit
) {
    Rect3DCard(
        shape = RoundedCornerShape(25.dp),
        contentPadding = 12.dp,
        faceVisible = visible,
        modifier = modifier.wrapContentWidth(),
        onClick = onClick
    ) {
        Text(
            text = word,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.Black800,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}