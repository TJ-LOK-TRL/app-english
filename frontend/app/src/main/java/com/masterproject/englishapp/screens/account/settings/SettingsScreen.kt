package com.masterproject.englishapp.screens.account.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.components.AppIcon
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.cards.Rect3DCard
import com.masterproject.englishapp.components.loaders.LoadingScreen
import com.masterproject.englishapp.screens.account.AccountPage
import com.masterproject.englishapp.screens.account.components.SettingLinkField
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.user.UserModel
import com.masterproject.englishapp.utils.DummyUserModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigate: (AccountPage) -> Unit,
    goBack: () -> Unit
) {
    val user by viewModel.userState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (user != null) {
        SettingsScreenContent(
            user!!,
            onNavigate,
            onLogoutClick = { showLogoutDialog = true }
        )

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Terminar Sessão") },
                text = { Text("Tens a certeza que pretendes sair da tua conta?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            viewModel.performLogout()
                            goBack()
                        }
                    ) { Text("Sair", color = AppColors.LightRed) }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
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
fun SettingsScreenContent(
    userModel: UserModel,
    onNavigate: (AccountPage) -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Rect3DCard(
            modifier = Modifier.clickable {  },
            shape = RoundedCornerShape(14.dp),
            borderWidth = 1.dp,
            borderColor = AppColors.Gray200,
            depthColor = AppColors.Gray300,
            onClick = { onNavigate(AccountPage.EDIT_PROFILE) }
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = userModel.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = userModel.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                AppIcon(
                    resId = R.drawable.ic_arrow_thin_chevron,
                    size = 18.dp,
                    tint = AppColors.Gray800
                )
            }
        }

        Rect3DCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                SettingLinkField(R.drawable.ic_settings, "Preferences") {
                    onNavigate(AccountPage.PREFERENCES)
                }
                SettingLinkField(R.drawable.ic_bell, "Notification") {
                    onNavigate(AccountPage.NOTIFICATIONS)
                }
                SettingLinkField(R.drawable.ic_hyperlink, "Linked Accounts") {
                    onNavigate(AccountPage.LINKED_ACCOUNTS)
                }
                SettingLinkField(R.drawable.ic_shield_checkmark, "Account & Security") {
                    onNavigate(AccountPage.ACCOUNT_SECURITY)
                }
                SettingLinkField(R.drawable.ic_terms_conditions, "Terms of Service") {
                    onNavigate(AccountPage.TERMS_OF_SERVICE)
                }
                SettingLinkField(R.drawable.ic_web_security, "Privacy Policy") {
                    onNavigate(AccountPage.PRIVACY_POLICY)
                }
            }
        }

        Rect3DCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                SettingLinkField(
                    R.drawable.ic_logout,
                    "Logout",
                    textColor = AppColors.LightRed,
                    tintColor = AppColors.LightRed,
                    linkResId = null,
                    onClick = onLogoutClick
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
fun SettingsScreenPreview() = SettingsScreenContent(DummyUserModel, {}) { }