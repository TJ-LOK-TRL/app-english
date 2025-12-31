package com.masterproject.englishapp.screens.auth.login

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.textinputs.IconTextField
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun showToast(message: String?) {
        Toast.makeText(context, message ?: "Unknown error", Toast.LENGTH_LONG).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Welcome back!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Black800
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.hello),
                    contentDescription = null,
                    Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            }

            Text(
                text = "Let's pick up where you left off in your learning adventure.",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        // Login fields
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            IconTextField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                iconRes = R.drawable.email
            )

            IconTextField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                iconRes = R.drawable.password,
                visualTransformation = PasswordVisualTransformation()
            )
        }

        // Remember and Forgot Password
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppColors.Primary,
                        uncheckedColor = AppColors.Primary,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = AppColors.Primary.copy(alpha = 0.38f),
                        disabledUncheckedColor = AppColors.Primary.copy(alpha = 0.38f),
                        disabledIndeterminateColor = AppColors.Primary.copy(alpha = 0.38f)
                    )
                )
                Text("Remember me")
            }

            Text(
                text = "Forgot Password?",
                color = AppColors.Primary,
                modifier = Modifier.clickable { /* ação */ }
            )
        }

        // Separator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text("  or continue with  ", color = Color(0xFF666666))
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        // Social login buttons
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SocialButton(R.drawable.gmail, modifier = Modifier.weight(1f))
            SocialButton(R.drawable.apple, modifier = Modifier.weight(1f))
            SocialButton(R.drawable.twitter, modifier = Modifier.weight(1f))
            SocialButton(R.drawable.facebook, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer Sign in button
        PrimaryButton(
            "Sign In",
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(email, password) { result ->
                        when(result) {
                            is AppResult.Success<*> -> onLoginSuccess()
                            is AppResult.Error -> showToast(result.error.message)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        )
    }
}

@Composable
fun SocialButton(iconId: Int, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { /* ação */ },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = AppColors.Gray200),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                Modifier.size(22.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen {  }
}