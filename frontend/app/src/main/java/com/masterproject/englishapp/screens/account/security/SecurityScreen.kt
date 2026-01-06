package com.masterproject.englishapp.screens.account.security

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.components.cards.Rect3DCard
import com.masterproject.englishapp.components.loaders.LoadingScreen
import com.masterproject.englishapp.components.loaders.OverlayLoader
import com.masterproject.englishapp.screens.account.components.SettingLinkField
import com.masterproject.englishapp.screens.account.profile.ProfileScreenContent
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun SecurityScreen(
    viewModel: SecurityViewModel = hiltViewModel(),
    onAccountDeleted: () -> Unit
) {
    val user by viewModel.userState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (user != null) {
        SecurityScreenContent(
            onChangePassword = { viewModel.sendPasswordReset() },
            onDeleteClick = { showDeleteDialog = true }
        )

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar Conta?") },
                text = { Text("Esta ação é permanente e apagará todo o seu progresso no inglês.") },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            showDeleteDialog = false
                            viewModel.deleteAccount(onAccountDeleted)
                        }
                    ) { Text("Eliminar", color = AppColors.LightRed) }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    } else {
        LoadingScreen()
    }
}

@Composable
fun SecurityScreenContent(
    onChangePassword: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Rect3DCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                SettingLinkField(
                    label = "Change Password",
                    onClick = onChangePassword
                )

                SettingLinkField(
                    label = "Delete Account",
                    textColor = AppColors.LightRed,
                    description = "Permantently remove your account and data. Proceed with caution.",
                    onClick = onDeleteClick
                )
            }
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
fun SecurityScreenPreview() = SecurityScreenContent({}, {})