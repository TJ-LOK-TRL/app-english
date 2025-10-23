// components/CommonHeader.kt
package com.masterproject.englishapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.R

/**
 * CommonHeader - Reusable header component with back button and bottom shadow
 *
 * @param title The title to display in the header
 * @param onBackClick Callback when back button is clicked
 * @param showBackButton Whether to show the back button (default: true)
 * @param backgroundColor Background color of the header
 * @param contentColor Color of the text and icons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonHeader(
    title: String,
    onBackClick: (() -> Unit)? = null,
    showBackButton: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
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
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor
                )
            },
            navigationIcon = {
                if (showBackButton && onBackClick != null) {
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
                } else if (showBackButton) {
                    // Placeholder for spacing when back button should be shown but no click handler
                    Spacer(modifier = Modifier.width(48.dp))
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

/**
 * Alternative header with custom content and stronger shadow
 *
 * @param onBackClick Callback when back button is clicked
 * @param content Custom content to display in the header
 * @param showBackButton Whether to show the back button
 */
@Composable
fun CommonHeaderWithContent(
    onBackClick: (() -> Unit)? = null,
    showBackButton: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 12.dp,
                ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                spotColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                clip = false
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton && onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.mic),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            content()
        }

        // Stronger bottom shadow line
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

// Preview for development
@Preview(showBackground = true)
@Composable
fun CommonHeaderPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            CommonHeader(
                title = "English Learning",
                onBackClick = { /* Preview only */ },
                showBackButton = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonHeaderWithoutBackButtonPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            CommonHeader(
                title = "Home Screen",
                onBackClick = null,
                showBackButton = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonHeaderWithContentPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            CommonHeaderWithContent(
                onBackClick = { /* Preview only */ },
                showBackButton = true
            ) {
                Text(
                    text = "Custom Header",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}