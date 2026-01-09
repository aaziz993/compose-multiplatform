package clib.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
public fun systemColorScheme(
    lightColorScheme: ColorScheme? = lightColorScheme(),
    darkColorScheme: ColorScheme? = darkColorScheme(),
): ColorScheme? = if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme
