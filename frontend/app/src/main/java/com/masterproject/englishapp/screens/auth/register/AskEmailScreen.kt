package com.masterproject.englishapp.screens.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.textinputs.IconTextField
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.utils.Validators


@Composable
fun AskEmailScreen(
    name: String,
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text="Thanks, ${name}! What's your email address?",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = AppColors.Black800
        )

        IconTextField(
            label = "Your Email",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "Email",
            iconRes = R.drawable.email
        )

        if (email.isNotEmpty() && !Validators.isValidEmail(email)) {
            Text(
                text = "Please enter a valid email address",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
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
fun AskEmailScreenPreview() = AskEmailScreen("Tyrese", "") {  }
