package clib.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import clib.presentation.theme.model.ColorPalette
import clib.presentation.theme.model.toColorScheme

public object DefaultTheme {

    public val ColorPalette: ColorPalette = ColorPalette(
        lightColorScheme = darkColorScheme().toColorScheme(),
        darkColorScheme = lightColorScheme().toColorScheme(),
    )
}
