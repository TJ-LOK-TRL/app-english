package com.masterproject.englishapp.screens.account.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun SettingLinkField(
    resId: Int? = null,
    label: String,
    description: String? = null,
    textColor: Color = Color.Black,
    tintColor: Color = Color.Black,
    linkResId: Int? = R.drawable.ic_arrow_thin_chevron,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 19.dp, horizontal = 12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                resId?.let {
                    AppIcon(resId = resId, size = 20.dp, tint = tintColor)
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            }

            linkResId?.let {
                AppIcon(
                    resId = linkResId,
                    size = 18.dp,
                    tint = AppColors.Gray800
                )
            }
        }

        description?.let {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.Gray600
            )
        }
    }
}