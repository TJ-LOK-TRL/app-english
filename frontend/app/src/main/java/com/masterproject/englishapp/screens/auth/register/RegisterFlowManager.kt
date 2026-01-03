package com.masterproject.englishapp.screens.auth.register

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.components.animations.AnimatedStepContent
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.headers.ProgressHeader
import com.masterproject.englishapp.components.loaders.LoadingScreen
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.utils.DummyNavigator
import com.masterproject.englishapp.utils.Validators

enum class RegisterStep { NAME, EMAIL, PASSWORD, END }

@Composable
fun RegisterFlowManager(
    navigator: NavigationActions,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var flowFinished by remember { mutableStateOf(false) }

    if (viewModel.isLoading) {
        LoadingScreen()
    } else {
        RegisterFlowContent(
            navigator,
            initialStep = if (flowFinished) RegisterStep.END else RegisterStep.NAME,
            onRegisterClick = { n, e, p ->
                viewModel.register(n, e, p) { result ->
                    when (result) {
                        is AppResult.Success -> {
                            flowFinished = true
                        }
                        is AppResult.Error -> {
                            Toast.makeText(
                                context,
                                "Error: ${result.error.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun RegisterFlowContent(
    navigator: NavigationActions,
    initialStep: RegisterStep,
    onRegisterClick: (String, String, String) -> Unit
) {
    var currentStep by remember(initialStep) { mutableStateOf(initialStep) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    val enableButton = when (currentStep) {
        RegisterStep.NAME -> name.trim().length >= 2
        RegisterStep.EMAIL -> Validators.isValidEmail(email)
        RegisterStep.PASSWORD -> password.length >= 6 && password == passwordConfirm
        RegisterStep.END -> true
    }

    val progress by animateFloatAsState(
        targetValue = when (currentStep) {
            RegisterStep.NAME -> 0.33f
            RegisterStep.EMAIL -> 0.66f
            RegisterStep.PASSWORD -> 1.0f
            else -> 0.0f
        },
        label = "ProgressAnimation"
    )

    val showProgressHeader = when(currentStep) {
        RegisterStep.END -> false
        else -> true
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showProgressHeader) {
            ProgressHeader(progress) {
                if (currentStep == RegisterStep.EMAIL) currentStep = RegisterStep.NAME
                else if (currentStep == RegisterStep.PASSWORD) currentStep = RegisterStep.EMAIL
            }
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

                RegisterStep.END -> RegisterEnder()
            }
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            enabled = enableButton,
            text = if (currentStep == RegisterStep.PASSWORD) "Finish" else "Continue",
            onClick = {
                when (currentStep) {
                    RegisterStep.NAME -> if (name.isNotBlank()) currentStep = RegisterStep.EMAIL
                    RegisterStep.EMAIL -> if (email.contains("@")) currentStep = RegisterStep.PASSWORD
                    RegisterStep.PASSWORD -> {
                        onRegisterClick(name, email, password)
                        currentStep = RegisterStep.END
                    }
                    RegisterStep.END -> navigator.navigate(Screen.HOME)
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
fun RegisterFlowManagerPreview() = RegisterFlowContent(DummyNavigator, RegisterStep.NAME) { _, _, _ -> }