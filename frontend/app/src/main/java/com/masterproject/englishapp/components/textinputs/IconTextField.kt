package com.masterproject.englishapp.components.textinputs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun IconTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    iconRes: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var isFocused by remember { mutableStateOf(false) }

    val lineColor by animateColorAsState(
        targetValue = if (isFocused) AppColors.PrimaryLight else AppColors.Gray400,
        label = "lineColor"
    )

    val lineThickness by animateDpAsState(
        targetValue = if (isFocused) 2.dp else 1.dp,
        label = "lineThickness"
    )

    Column {
        Text(
            label,
            fontWeight = FontWeight.Bold,
            color = AppColors.Black800
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            iconRes?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = null,
                    tint = lineColor,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(placeholder) },  // ou placeholder = { Text(placeholder) }
                singleLine = true,
                visualTransformation = visualTransformation,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.offset(y = (-5).dp),
            thickness = lineThickness,
            color = lineColor,
        )
    }
}