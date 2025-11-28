package clib.presentation.theme.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import clib.presentation.theme.shapes.Shapes
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Theme(
    val isDark: Boolean? = null,
    val isDynamic: Boolean = false,
    val isHighContrast: Boolean = false,
    val colorPalette: ColorPalette = ColorPalette(),
    val colorPaletteHighContrast: ColorPalette = ColorPalette(),
    val dynamicColorPalette: DynamicColorPalette? = null,
    val dynamicColorPaletteHighContrast: DynamicColorPalette? = null,
    val shapes: Shapes? = null,
) {

    public fun copyIsDarkToggled(): Theme = when (isDark) {
        null -> copy(isDark = false)
        false -> copy(isDark = true)
        true -> copy(isDark = null)
    }

    @Suppress("ComposeUnstableReceiver")
    @Composable
    public fun isDark(): Boolean = isDark ?: isSystemInDarkTheme()
}

