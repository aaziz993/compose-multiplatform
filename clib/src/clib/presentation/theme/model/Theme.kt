package clib.presentation.theme.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import clib.presentation.theme.darkHighContrast
import clib.presentation.theme.lightHighContrast
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
    val lightColorScheme: ColorSchemeSerial = lightColorScheme(),
    val lightColorSchemeHighContrast: ColorSchemeSerial = lightColorScheme.lightHighContrast(),
    val darkColorScheme: ColorSchemeSerial = darkColorScheme(),
    val darkColorSchemeHighContrast: ColorSchemeSerial = darkColorScheme.darkHighContrast(),
    val dynamicColorScheme: DynamicColorScheme = DynamicColorScheme(Color.Cyan),
    val dynamicColorSchemeHighContrast: DynamicColorScheme = DynamicColorScheme(Color.Blue),
    val shapes: ShapesSerial = Shapes(),
    val typography: TypographySerial = Typography(),
) {

    public val currentColorScheme: ColorScheme
        @Composable
        get() {
            val (lightColorScheme, darkColorScheme) =
                if (isHighContrast) lightColorScheme to lightColorSchemeHighContrast
                else darkColorScheme to darkColorSchemeHighContrast
            return if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme
        }

    public val currentDynamicColorScheme: DynamicColorScheme
        get() = if (isHighContrast) dynamicColorSchemeHighContrast else dynamicColorScheme

    public fun copyIsDarkToggled(): Theme = when (isDark) {
        null -> copy(isDark = false)
        false -> copy(isDark = true)
        true -> copy(isDark = null)
    }

    @Composable
    public fun copyColorScheme(): (ColorScheme) -> Theme {
        val isDark = isDark ?: isSystemInDarkTheme()
        return if (isDark) {
            if (isHighContrast) { colorScheme -> copy(darkColorSchemeHighContrast = colorScheme) }
            else { colorScheme -> copy(darkColorScheme = colorScheme) }
        }
        else {
            if (isHighContrast) { colorScheme -> copy(lightColorSchemeHighContrast = colorScheme) }
            else { colorScheme -> copy(lightColorScheme = colorScheme) }
        }
    }

    public fun copyDynamicColorScheme(colorScheme: DynamicColorScheme): Theme =
        if (isHighContrast) copy(dynamicColorSchemeHighContrast = colorScheme) else copy(dynamicColorScheme = colorScheme)
}

