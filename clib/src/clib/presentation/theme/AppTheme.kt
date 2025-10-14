@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.key
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode

public val LocalAppTheme: ProvidableCompositionLocal<Theme> = staticCompositionLocalOf { noLocalProvidedFor("LocalTheme") }

@Composable
public fun AppTheme(
    theme: Theme,
    lightColorScheme: ColorScheme = LightColors,
    lightColorSchemeHighContrast: ColorScheme = LightColorsHighContrast,
    darkColorScheme: ColorScheme = DarkColors,
    darkColorSchemeHighContrast: ColorScheme = DarkColorsHighContrast,
    content: @Composable (ColorScheme) -> Unit,
) {
    val (light, dark) =
        if (theme.isHighContrast) lightColorSchemeHighContrast to darkColorSchemeHighContrast else lightColorScheme to darkColorScheme

    val colorScheme = when (theme.mode) {
        ThemeMode.SYSTEM -> systemTheme(light, dark)
        ThemeMode.LIGHT -> light
        ThemeMode.DARK -> dark
    }

    CompositionLocalProvider(
        LocalAppTheme provides theme,
    ) {
        content(colorScheme)
    }
}
