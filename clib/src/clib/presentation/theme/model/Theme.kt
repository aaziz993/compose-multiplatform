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
import clib.presentation.theme.isAdaptiveDark
import clib.presentation.theme.lightHighContrast
import clib.presentation.theme.shapes.ShapesSerial
import clib.presentation.theme.typography.TypographySerial
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Theme(
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val lightTime: LocalTime = LocalTime(6, 0),
    val darkTime: LocalTime = LocalTime(19, 0),
    val isHighContrast: Boolean = false,
    val isDynamic: Boolean = false,
    val lightColorScheme: ColorSchemeSerial = lightColorScheme(),
    val lightColorSchemeHighContrast: ColorSchemeSerial = lightColorScheme.lightHighContrast(),
    val darkColorScheme: ColorSchemeSerial = darkColorScheme(),
    val darkColorSchemeHighContrast: ColorSchemeSerial = darkColorScheme.darkHighContrast(),
    val dynamicColorScheme: DynamicColorScheme = DynamicColorScheme(Color.Cyan),
    val dynamicColorSchemeHighContrast: DynamicColorScheme = DynamicColorScheme(Color.Blue),
    val isExpressive: Boolean = true,
    val shapes: ShapesSerial = Shapes(),
    val typography: TypographySerial = Typography(),
) {

    public val currentColorScheme: ColorScheme
        @Composable
        get() {
            val (lightColorScheme, darkColorScheme) =
                if (isHighContrast) lightColorSchemeHighContrast to darkColorSchemeHighContrast
                else lightColorScheme to darkColorScheme
            return if (isDark()) darkColorScheme else lightColorScheme
        }

    public val currentDynamicColorScheme: DynamicColorScheme
        get() = if (isHighContrast) dynamicColorSchemeHighContrast else dynamicColorScheme

    @Composable
    public fun isDark(): Boolean =
        when (mode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.ADAPTIVE -> isAdaptiveDark(lightTime, darkTime)
        }

    public fun copyIsDarkToggled(): Theme = when (mode) {
        ThemeMode.SYSTEM -> copy(mode = ThemeMode.LIGHT)
        ThemeMode.LIGHT -> copy(mode = ThemeMode.DARK)
        ThemeMode.DARK -> copy(mode = ThemeMode.ADAPTIVE)
        ThemeMode.ADAPTIVE -> copy(mode = ThemeMode.SYSTEM)
    }

    @Composable
    public fun copyColorScheme(): (ColorScheme) -> Theme =
        if (isHighContrast) {
            if (isDark()) { colorScheme -> copy(darkColorSchemeHighContrast = colorScheme) }
            else { colorScheme -> copy(lightColorSchemeHighContrast = colorScheme) }
        }
        else {
            if (isDark()) { colorScheme -> copy(darkColorScheme = colorScheme) }
            else { colorScheme -> copy(lightColorScheme = colorScheme) }
        }

    public fun copyDynamicColorScheme(colorScheme: DynamicColorScheme): Theme =
        if (isHighContrast) copy(dynamicColorSchemeHighContrast = colorScheme) else copy(dynamicColorScheme = colorScheme)
}
