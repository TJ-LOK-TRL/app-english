// MainActivity.kt
package com.masterproject.englishapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.permissions.PermissionManager
import com.masterproject.englishapp.screens.main.MainLandingPage
import com.masterproject.englishapp.screens.main.MainViewModel
import com.masterproject.englishapp.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

/**
 * MainActivity - Entry point of the English Learning Application
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionManager: PermissionManager

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionManager = PermissionManager(this)

        handleIntent()

        // Set up Compose UI
        setContent {
            MyApplicationTheme {
                MainLandingPage(
                    permissionManager = permissionManager,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent()
    }

    private fun handleIntent() {
        intent?.getStringExtra("target_screen")?.let { screen ->
            mainViewModel.launchScreen(Screen.fromRoute(screen))
        }
    }
}