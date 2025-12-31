package com.masterproject.englishapp.screens.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.components.textinputs.IconTextField
import com.masterproject.englishapp.ui.theme.AppColors


@Composable
fun AskNameScreen(
    name: String,
    onNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text="First, what should we call you?",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = AppColors.Black800
        )

        IconTextField(
            label = "Your Name",
            value = name,
            onValueChange = onNameChange,
            placeholder = "Name"
        )
    }
}

@Preview(
    name = "Xiaomi Redmi 9C",
    device = "spec:width=360dp,height=800dp,dpi=269",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEEEEEE
)
@Composable
fun AskNameScreenPreview() = AskNameScreen("") {  }
