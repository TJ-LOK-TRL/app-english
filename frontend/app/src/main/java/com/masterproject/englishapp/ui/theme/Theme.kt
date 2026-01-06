package com.masterproject.englishapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

object AppColors {
    // Main colors
    val Primary = Color(0xFFFD5818)
    val PrimaryLight = Color(0xFFFF8A50)
    val PrimaryDark = Color(0xFFC41C00)

    // Grays
    val Gray50 = Color(0xFFFAFAFA)
    val Gray100 = Color(0xFFF5F5F5)
    val Gray200 = Color(0xFFEEEEEE)
    val Gray300 = Color(0xFFE0E0E0)
    val Gray400 = Color(0xFFBDBDBD)
    val Gray500 = Color(0xFF9E9E9E)
    val Gray600 = Color(0xFF757575)
    val Gray700 = Color(0xFF616161)
    val Gray800 = Color(0xFF424242)
    val Gray900 = Color(0xFF212121)

    // Blacks
    val Black50  = Color(0xFFF5F5F5)
    val Black100 = Color(0xFFE0E0E0)
    val Black200 = Color(0xFFBDBDBD)
    val Black300 = Color(0xFF9E9E9E)
    val Black400 = Color(0xFF757575)
    val Black500 = Color(0xFF616161)
    val Black600 = Color(0xFF424242)
    val Black700 = Color(0xFF303030)
    val Black800 = Color(0xFF212121)
    val Black900 = Color(0xFF000000)

    val LightRed = Color.Red.copy(alpha = 0.8f)

    // System
    val Background = Color.White// Color(0xFFEEEEEE)
    val Surface = Color(0xFFFFFFFF)
    val Error = Color(0xFFD32F2F)
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF000000)
}

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Primary,
    secondary = AppColors.Gray600,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    secondary = AppColors.Gray600,
    background = AppColors.Background,
    surface = AppColors.Surface
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}