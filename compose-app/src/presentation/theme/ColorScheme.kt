package presentation.theme

import androidx.compose.material3.ColorScheme
import clib.presentation.theme.BlackTheme
import clib.presentation.theme.WhiteBlueTheme
import clib.presentation.theme.darkHighContrast
import clib.presentation.theme.lightHighContrast

public val LightColorScheme: ColorScheme = WhiteBlueTheme.colorScheme
public val LightColorSchemeHighContrast: ColorScheme = LightColorScheme.lightHighContrast()

public val DarkColorScheme: ColorScheme = BlackTheme.colorScheme
public val DarkColorSchemeHighContrast: ColorScheme = DarkColorScheme.darkHighContrast()
