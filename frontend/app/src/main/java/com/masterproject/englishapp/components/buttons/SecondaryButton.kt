package com.masterproject.englishapp.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.ui.theme.Typography

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderColor: Color = AppColors.Primary,
    textColor: Color = AppColors.Primary,
    backgroundColor: Color = AppColors.Gray200
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = AppColors.Gray200,
            disabledContentColor = AppColors.Gray500
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AppColors.Gray200 //if (enabled) borderColor else AppColors.Gray300
        ),
    ) {
        Text(
            text = text,
            style = Typography.titleMedium
        )
    }
}