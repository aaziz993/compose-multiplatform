package clib.presentation.theme.model

import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
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
    val dynamicColorPalette: DynamicColorPalette = DynamicColorPalette(Color.Cyan),
    val dynamicColorPaletteHighContrast: DynamicColorPalette = DynamicColorPalette(Color.Blue),
    val shapes: ShapesSerial = Shapes(),
    val typography: TypographySerial = Typography(),
) {

    public fun copyIsDarkToggled(): Theme = when (isDark) {
        null -> copy(isDark = false)
        false -> copy(isDark = true)
        true -> copy(isDark = null)
    }
}

