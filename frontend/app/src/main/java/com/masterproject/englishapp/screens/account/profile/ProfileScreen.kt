package com.masterproject.englishapp.screens.account.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.loaders.LoadingScreen
import com.masterproject.englishapp.components.loaders.OverlayLoader
import com.masterproject.englishapp.components.textinputs.IconTextField
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.user.UserModel
import com.masterproject.englishapp.utils.DummyUserModel
import com.masterproject.englishapp.utils.Validators

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.userState.collectAsState()
    var isSaving by remember { mutableStateOf(false) }

    if (user != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            ProfileScreenContent(user!!, isSaving) { updatedUser ->
                viewModel.performUpdate(updatedUser) { /* UiEventService used internally */ }
            }

            if (isSaving) {
                OverlayLoader()
            }
        }
    } else {
        LoadingScreen()
    }
}

@Composable
fun ProfileScreenContent(
    userModel: UserModel,
    isSaving: Boolean,
    updateUserModel: (UserModel) -> Unit
) {
    var userDraft by remember(userModel) { mutableStateOf(userModel) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = AppColors.Primary.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Name first letter in uppercase
                        Text(
                            text = userModel.name.take(1).uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = AppColors.Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconTextField(
                    label = "Your Name",
                    value = userDraft.name,
                    onValueChange = { userDraft = userDraft.copy(name = it) },
                    placeholder = "Name",
                )

                IconTextField(
                    label = "Your Email",
                    value = userDraft.email,
                    onValueChange = { userDraft = userDraft.copy(email = it) },
                    placeholder = "Email",
                    iconRes = R.drawable.email
                )

                if (!Validators.isValidEmail(userDraft.email)) {
                    Text(
                        text = "Please enter a valid email address",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = if (isSaving) "A guardar..." else "Salvar",
            enabled = !isSaving && userDraft != userModel && Validators.isValidEmail(userDraft.email),
            onClick = { updateUserModel(userDraft) }
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
fun ProfileScreenPreview() = ProfileScreenContent(DummyUserModel, false) { }