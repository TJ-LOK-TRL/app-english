package com.masterproject.englishapp.screens.auth.register

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.components.animations.AnimatedStepContent
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.loaders.LoadingScreen
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.utils.Validators

enum class RegisterStep { NAME, EMAIL, PASSWORD }

@Composable
fun RegisterFlowManager(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    if (viewModel.isLoading) {
        LoadingScreen()
    } else {
        RegisterFlowContent(
            onRegisterClick = { n, e, p ->
                viewModel.register(n, e, p) { result ->
                    if (result is AppResult.Success) {
                        onRegisterSuccess()
                    }
                }
            }
        )
    }
}

@Composable
fun RegisterFlowContent(
    onRegisterClick: (String, String, String) -> Unit
) {
    var currentStep by remember { mutableStateOf(RegisterStep.NAME) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    val isPasswordValid = password.length >= 6 && password == passwordConfirm

    val enableButton = when (currentStep) {
        RegisterStep.NAME -> name.trim().length >= 2
        RegisterStep.EMAIL -> Validators.isValidEmail(email)
        RegisterStep.PASSWORD -> password.length >= 6 && password == passwordConfirm
    }

    val progress by animateFloatAsState(
        targetValue = when (currentStep) {
            RegisterStep.NAME -> 0.33f
            RegisterStep.EMAIL -> 0.66f
            RegisterStep.PASSWORD -> 1.0f
        },
        label = "ProgressAnimation"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (currentStep == RegisterStep.EMAIL) currentStep = RegisterStep.NAME
                    else if (currentStep == RegisterStep.PASSWORD) currentStep = RegisterStep.EMAIL
                }
            ) {
                AppIcon(resId = R.drawable.arrow_back, size = 20.dp, tint = AppColors.Gray700)
            }

            Spacer(modifier = Modifier.width(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(8.dp),
                color = AppColors.Primary,
                trackColor = AppColors.Gray300,
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = { }
            )
        }

        AnimatedStepContent(
            targetState = currentStep,
            modifier = Modifier.weight(1f)
        ) { targetStep ->
            when (targetStep) {
                RegisterStep.NAME -> AskNameScreen(
                    name = name,
                    onNameChange = { name = it }
                )
                RegisterStep.EMAIL -> AskEmailScreen(
                    name = name,
                    email = email,
                    onEmailChange = { email = it }
                )
                RegisterStep.PASSWORD -> AskPasswordScreen(
                    password = password,
                    onPasswordChange = { password = it },
                    passwordConfirm = passwordConfirm,
                    onPasswordConfirmChange = { passwordConfirm = it }
                )
            }
        }

        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = enableButton,
            text = if (currentStep == RegisterStep.PASSWORD) "Finish" else "Continue",
            onClick = {
                when (currentStep) {
                    RegisterStep.NAME -> if (name.isNotBlank()) currentStep = RegisterStep.EMAIL
                    RegisterStep.EMAIL -> if (email.contains("@")) currentStep = RegisterStep.PASSWORD
                    RegisterStep.PASSWORD -> {
                        if (isPasswordValid) {
                            onRegisterClick(name, email, password)
                        }
                    }
                }
            }
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
fun RegisterFlowManagerPreview() = RegisterFlowContent { _, _, _ -> }