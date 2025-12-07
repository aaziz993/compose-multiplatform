package presentation.theme

import clib.presentation.theme.BlackTheme
import clib.presentation.theme.WhiteBlueTheme
import clib.presentation.theme.darkHighContrast
import clib.presentation.theme.lightHighContrast
import clib.presentation.theme.model.ColorPalette

public val colorPalette: ColorPalette = ColorPalette(
    WhiteBlueTheme.colorScheme,
    BlackTheme.colorScheme,
)

public val colorPaletteHighContrast: ColorPalette = ColorPalette(
    WhiteBlueTheme.colorScheme.lightHighContrast(),
    BlackTheme.colorScheme.darkHighContrast(),
)
