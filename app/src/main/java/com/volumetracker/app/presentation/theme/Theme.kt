package com.volumetracker.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF00CC66),
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFF4DFFB8),
    onSecondary = Color(0xFF000000),
    error = AlertRed,
    onError = Color(0xFFFFFFFF),
    background = DarkBackground,
    onBackground = Color(0xFFE0E0E0),
    surface = DarkSurface,
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB0B0B0)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00CC66),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = NeonGreen,
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFF00AA55),
    onSecondary = Color(0xFFFFFFFF),
    error = Color(0xFFCC0000),
    onError = Color(0xFFFFFFFF),
    background = LightBackground,
    onBackground = Color(0xFF1C1C1C),
    surface = LightSurface,
    onSurface = Color(0xFF1C1C1C),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF4A4A4A)
)

@Composable
fun VolumeTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
