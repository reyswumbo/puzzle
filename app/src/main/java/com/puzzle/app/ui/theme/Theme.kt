package com.puzzle.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val BoardGameOrange = Color(0xFFFF6B35)
private val BoardGameDark = Color(0xFF1A1A2E)
private val BoardGameBrown = Color(0xFF5C3D2E)
private val BoardGameBeige = Color(0xFFF5E6D3)
private val BoardGameGreen = Color(0xFF2D6A4F)
private val BoardGameGold = Color(0xFFFFB703)

private val DarkColorScheme = darkColorScheme(
    primary = BoardGameOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF92400E),
    onPrimaryContainer = Color.White,
    secondary = BoardGameGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1B4332),
    onSecondaryContainer = Color.White,
    tertiary = BoardGameGold,
    onTertiary = Color.Black,
    background = BoardGameDark,
    onBackground = BoardGameBeige,
    surface = Color(0xFF16213E),
    onSurface = BoardGameBeige,
    surfaceVariant = Color(0xFF1A1A3E),
    onSurfaceVariant = Color(0xFFD4C4B0),
)

private val LightColorScheme = lightColorScheme(
    primary = BoardGameBrown,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4A574),
    onPrimaryContainer = BoardGameBrown,
    secondary = BoardGameGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB7E4C7),
    onSecondaryContainer = Color(0xFF1B4332),
    tertiary = BoardGameGold,
    onTertiary = Color.Black,
    background = BoardGameBeige,
    onBackground = BoardGameBrown,
    surface = Color(0xFFFFFBF5),
    onSurface = BoardGameBrown,
    surfaceVariant = Color(0xFFF0E4D7),
    onSurfaceVariant = Color(0xFF5C3D2E),
)

@Composable
fun PuzzleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
