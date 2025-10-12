package clib.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

public val LightColors: ColorScheme = lightColorScheme()
public val LightColorsHighContrast: ColorScheme = LightColors.lightColorsHighContrast()

public fun ColorScheme.lightColorsHighContrast(): ColorScheme = copy(
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

public val DarkColors: ColorScheme = darkColorScheme()
public val DarkColorsHighContrast: ColorScheme = DarkColors.darkColorsHighContrast()

public fun ColorScheme.darkColorsHighContrast(): ColorScheme = copy(
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
    lightColorScheme: ColorScheme = LightColors,
    darkColorScheme: ColorScheme = DarkColors
): ColorScheme = if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme

