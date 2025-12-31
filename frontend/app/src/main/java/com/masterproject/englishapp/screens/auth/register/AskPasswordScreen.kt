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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.textinputs.IconTextField
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun AskPasswordScreen(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordConfirm: String,
    onPasswordConfirmChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "One last step! Create a password to keep your account safe and secure.",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = AppColors.Black800
        )

        IconTextField(
            label = "Password",
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "At least 6 characters",
            iconRes = R.drawable.password,
            visualTransformation = PasswordVisualTransformation()
        )

        IconTextField(
            label = "Confirm Password",
            value = passwordConfirm,
            onValueChange = onPasswordConfirmChange,
            placeholder = "Repeat your password",
            iconRes = R.drawable.password,
            visualTransformation = PasswordVisualTransformation()
        )

        if (passwordConfirm.isNotEmpty() && password != passwordConfirm) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        if (password.isNotEmpty() && password.length < 6) {
            Text(
                text = "Password must have at least 6 characters",
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
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
fun AskPasswordScreenPreview() = AskPasswordScreen(
    "",
    { },
    "",
    {}
)