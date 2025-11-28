package clib.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import clib.presentation.theme.model.ColorPalette

public object DefaultTheme {

    public val ColorPalette: ColorPalette = ColorPalette(
        lightColorScheme = darkColorScheme(),
        darkColorScheme = lightColorScheme(),
    )
}
