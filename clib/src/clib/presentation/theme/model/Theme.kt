package clib.presentation.theme.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import clib.presentation.theme.darkHighContrast
import clib.presentation.theme.lightHighContrast
import clib.presentation.theme.shapes.ShapesSerial
import clib.presentation.theme.typography.TypographySerial
import klib.data.type.primitives.time.now
import klib.data.type.primitives.time.toLocalDateTime
import klib.data.type.primitives.time.toLocalTime
import klib.data.type.serialization.plus
import kotlin.time.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import pro.respawn.kmmutils.datetime.plusDays

@Immutable
@Serializable
public data class Theme(
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val lightModeTime: LocalTime = LocalTime(6, 0),
    val darkModeTime: LocalTime = LocalTime(19, 0),
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
                if (isHighContrast) lightColorSchemeHighContrast to darkColorSchemeHighContrast
                else lightColorScheme to darkColorScheme
            return if (isDark()) darkColorScheme else lightColorScheme
        }

    public val currentDynamicColorScheme: DynamicColorScheme
        get() = if (isHighContrast) dynamicColorSchemeHighContrast else dynamicColorScheme

    @Composable
    public fun isDark(): Boolean = isSystemInDarkTheme()

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

    //    @Suppress("ComposeNamingUppercase")
//    @Composable
//    public fun isAdaptiveDark(): Boolean {
//        produceState(isNighTime()) {
//            while (true) {
//                val now = LocalDateTime.now()
//
//                val nextChange = when {
//                    now.hour < 6 -> LocalTime(6, 0, 0)
//                    now.hour < 19 -> LocalTime(19, 0, 0)
//                    else -> now.plusDays(1).withH LocalTime (1
//                        , 6, 0)
//                }
//            }
//        }
//    }
//
    private fun isNighTime(): Boolean {
        val hour = LocalTime.now(TimeZone.currentSystemDefault()).hour
        return hour < lightModeTime.hour || hour >= darkModeTime.hour
    }
}
