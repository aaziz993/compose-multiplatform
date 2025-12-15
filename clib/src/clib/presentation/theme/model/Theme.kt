package clib.presentation.theme.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import clib.presentation.theme.darkHighContrast
import clib.presentation.theme.lightHighContrast
import clib.presentation.theme.shapes.ShapesSerial
import clib.presentation.theme.typography.TypographySerial
import klib.data.type.primitives.time.toLocalTime
import kotlin.time.Clock
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import pro.respawn.kmmutils.datetime.plusDays
import pro.respawn.kmmutils.datetime.withTime

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
    public fun isDark(): Boolean =
        when (mode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.ADAPTIVE -> isAdaptiveDark()
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

@Suppress("ComposeNamingUppercase")
@Composable
public fun isAdaptiveDark(
    dayStart: LocalTime = LocalTime(6, 0),
    nightStart: LocalTime = LocalTime(19, 0),
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Boolean {
    val isNight by produceState(isNight(dayStart, nightStart, clock, timeZone)) {
        while (true) {
            val now = clock.now().toLocalDateTime(timeZone)

            val nextChange = when {
                now.time < dayStart -> now.withTime(dayStart)
                now.time < nightStart -> now.withTime(nightStart)
                else -> now.plusDays(1, TimeZone.currentSystemDefault())
                    .withTime(dayStart)
            }

            val delayMs = nextChange.toInstant(timeZone)
                .minus(clock.now())
                .inWholeMilliseconds
                .coerceAtLeast(0)

            delay(delayMs)
            value = isNight(dayStart, nightStart, clock, timeZone)
        }
    }

    return isNight
}

private fun isNight(
    dayStart: LocalTime,
    nightStart: LocalTime,
    clock: Clock,
    timeZone: TimeZone,
): Boolean {
    val now = clock.now().toLocalTime(timeZone)
    return now < dayStart || now >= nightStart
}
