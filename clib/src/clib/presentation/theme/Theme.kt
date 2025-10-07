package clib.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

public val lightColorScheme: ColorScheme = lightColorScheme()

public fun ColorScheme.lightHighContrast(): ColorScheme = copy(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color.Black,
    onPrimaryContainer = Color.White,
    surface = Color.White,
    onSurface = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    outline = Color.Black,
    outlineVariant = Color.Black,
)

public val darkColorScheme: ColorScheme = darkColorScheme()

public fun ColorScheme.darkHighContrast(): ColorScheme = copy(
    primary = Color.White,
    onPrimary = Color.Black,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.Black,
    surface = Color.Black,
    onSurface = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    outline = Color.White,
    outlineVariant = Color.White,
)

@Composable
public fun systemTheme(
    lightTheme: ColorScheme = lightColorScheme,
    darkTheme: ColorScheme = darkColorScheme
): ColorScheme = if (isSystemInDarkTheme()) darkTheme else lightTheme

public fun highContrastColorScheme(
    isDark: Boolean,
    lightTheme: ColorScheme = lightColorScheme,
    darkTheme: ColorScheme = darkColorScheme
): ColorScheme = if (isDark) darkTheme.darkHighContrast() else lightTheme.lightHighContrast()
