// components/CommonHeader.kt
package com.masterproject.englishapp.components.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masterproject.englishapp.R
import com.masterproject.englishapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonHeader(
    navController: NavHostController,
    onBackClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val currentScreen = Screen.fromRoute(currentRoute)
    val showBackButton = currentScreen != Screen.HOME

    Box(
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                ambientColor = MaterialTheme.colorScheme.outline,
                spotColor = MaterialTheme.colorScheme.outline
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    text = currentScreen.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor
                )
            },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Back",
                            tint = contentColor
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = contentColor,
                actionIconContentColor = contentColor,
                navigationIconContentColor = contentColor
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        // Bottom shadow line for enhanced elevation effect
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}