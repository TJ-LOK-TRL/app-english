package com.masterproject.englishapp.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.ui.theme.Typography

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.Primary,
            contentColor = AppColors.OnPrimary,
            disabledContainerColor = AppColors.Primary.copy(alpha = 0.5f),
            disabledContentColor = AppColors.OnPrimary.copy(alpha = 0.7f)
        ),
    ) {
        Text(
            text = text,
            style = Typography.titleMedium
        )
    }
}