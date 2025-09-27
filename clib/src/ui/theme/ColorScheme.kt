package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

public val lightColorScheme: ColorScheme = lightColorScheme()

public val darkColorScheme: ColorScheme = darkColorScheme()

@Composable
public fun systemColorScheme(): ColorScheme = if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme

