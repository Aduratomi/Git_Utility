package com.example.gitutility.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Theme.kt is the global styling engine for the app.
 * It decides which colors and fonts to use based on the phone's settings (like Dark Mode).
 */

// Colors used when the phone is in Dark Mode
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Colors used when the phone is in Light Mode
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun GitUtilityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Choose the color palette
    val colorScheme = when {
        // If the phone supports dynamic "Material You" colors
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Fallback to our custom dark/light palettes
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Apply the chosen scheme to the entire app
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
