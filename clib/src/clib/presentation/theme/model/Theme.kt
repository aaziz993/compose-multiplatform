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
import clib.data.type.primitives.color.ColorSerial
import clib.presentation.theme.isAdaptiveDark
import clib.presentation.theme.shapes.ShapesSerial
import clib.presentation.theme.typography.TypographySerial
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Theme(
    val lightColorScheme: ColorSchemeSerial = lightColorScheme(),
    val darkColorScheme: ColorSchemeSerial = darkColorScheme(),
    val dynamicColorScheme: DynamicColorScheme = DynamicColorScheme(Color.Cyan),
    val isDynamic: Boolean = false,
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val lightTime: LocalTime = LocalTime(6, 0),
    val darkTime: LocalTime = LocalTime(18, 0),
    val isAmoled: Boolean = false,
    val isHighContrast: Boolean = false,
    val isInvert: Boolean = false,
    val animate: Boolean = false,
    val animationSpec: AnimationSpecSerial<ColorSerial>? = null,
    val isExpressive: Boolean = true,
    val shapes: ShapesSerial = Shapes(),
    val typography: TypographySerial = Typography(),
) {

    public val colorScheme: ColorScheme
        @Composable
        get() = if (isDark()) darkColorScheme else lightColorScheme

    @Composable
    public fun copyColorSchemeFunc(): (ColorScheme) -> Theme =
        if (isDark()) { colorScheme -> copy(darkColorScheme = colorScheme) }
        else { colorScheme -> copy(lightColorScheme = colorScheme) }

    @Composable
    public fun isDark(): Boolean =
        when (mode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.ADAPTIVE -> isAdaptiveDark(lightTime, darkTime)
        }


    public fun copyToggledFunc(): () -> Theme {
//        val isSystemInDarkTheme: Boolean? = if (mode == ThemeMode.SYSTEM) isSystemInDarkTheme() else null
        return {
            when (mode) {
                ThemeMode.SYSTEM -> copy(mode = if (true) ThemeMode.LIGHT else ThemeMode.DARK)
                ThemeMode.LIGHT -> copy(mode = ThemeMode.DARK)
                ThemeMode.DARK -> copy(mode = ThemeMode.ADAPTIVE)
                ThemeMode.ADAPTIVE -> copy(mode = ThemeMode.SYSTEM)
            }
        }
    }
}
