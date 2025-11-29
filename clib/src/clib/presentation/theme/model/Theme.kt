package clib.presentation.theme.model

import androidx.compose.runtime.Immutable
import clib.presentation.theme.shapes.ShapesSerial
import clib.presentation.theme.typography.TypographySerial
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Theme(
    val isDark: Boolean? = null,
    val isDynamic: Boolean = false,
    val isHighContrast: Boolean = false,
    val isExpressive: Boolean = true,
    val colorPalette: ColorPalette = ColorPalette(),
    val colorPaletteHighContrast: ColorPalette = ColorPalette(),
    val dynamicColorPalette: DynamicColorPalette? = null,
    val dynamicColorPaletteHighContrast: DynamicColorPalette? = null,
    val shapes: ShapesSerial? = null,
    val typography: TypographySerial? = null,
) {

    public fun copyIsDarkToggled(): Theme = when (isDark) {
        null -> copy(isDark = false)
        false -> copy(isDark = true)
        true -> copy(isDark = null)
    }
}

