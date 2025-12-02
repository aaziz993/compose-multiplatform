package presentation.theme

import clib.presentation.theme.BlackTheme
import clib.presentation.theme.WhiteBlueTheme
import clib.presentation.theme.darkColorsHighContrast
import clib.presentation.theme.lightColorsHighContrast
import clib.presentation.theme.model.ColorPalette

public val colorPalette: ColorPalette = ColorPalette(
    WhiteBlueTheme.colorScheme,
    BlackTheme.colorScheme,
)

public val colorPaletteHighContrast: ColorPalette = ColorPalette(
    WhiteBlueTheme.colorScheme.lightColorsHighContrast(),
    BlackTheme.colorScheme.darkColorsHighContrast(),
)
