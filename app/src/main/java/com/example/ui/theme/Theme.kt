package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = AetherBlueDark,
    onPrimary = Color(0xFF00296E),
    primaryContainer = Color(0xFF003F9E),
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondary = AetherTealDark,
    onSecondary = Color(0xFF00382E),
    secondaryContainer = Color(0xFF005143),
    onSecondaryContainer = Color(0xFF70F8DC),
    tertiary = AetherAmberDark,
    onTertiary = Color(0xFF422C00),
    tertiaryContainer = Color(0xFF604100),
    onTertiaryContainer = Color(0xFFFFDF9E),
    error = AetherCoralDark,
    onError = Color(0xFF680005),
    background = AetherDarkBackground,
    onBackground = AetherDarkTextPrimary,
    surface = AetherDarkSurface,
    onSurface = AetherDarkTextPrimary,
    surfaceVariant = AetherDarkSurfaceVariant,
    onSurfaceVariant = AetherDarkTextSecondary,
    outline = AetherDarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = AetherBlue,
    onPrimary = Color.White,
    primaryContainer = AetherBlueLight,
    onPrimaryContainer = Color(0xFF001945),
    secondary = AetherTeal,
    onSecondary = Color.White,
    secondaryContainer = AetherTealLight,
    onSecondaryContainer = Color(0xFF00201A),
    tertiary = AetherAmber,
    onTertiary = Color.White,
    tertiaryContainer = AetherAmberLight,
    onTertiaryContainer = Color(0xFF281800),
    error = AetherCoral,
    onError = Color.White,
    errorContainer = AetherCoralLight,
    onErrorContainer = Color(0xFF410003),
    background = AetherLightBackground,
    onBackground = AetherLightTextPrimary,
    surface = AetherLightSurface,
    onSurface = AetherLightTextPrimary,
    surfaceVariant = AetherLightSurfaceVariant,
    onSurfaceVariant = AetherLightTextSecondary,
    outline = AetherLightOutline
)

@Composable
fun AetherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
