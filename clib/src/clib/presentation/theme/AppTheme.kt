@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode

public val LocalAppTheme: ProvidableCompositionLocal<Theme> = staticCompositionLocalOf { noLocalProvidedFor("LocalTheme") }

@Composable
public fun AppTheme(
    state: ThemeState,
    lightTheme: ColorScheme = lightColorScheme,
    lightThemeHighContrast: ColorScheme = lightColorSchemeHighContrast,
    darkTheme: ColorScheme = darkColorScheme,
    darkThemeHighContrast: ColorScheme = darkColorSchemeHighContrast,
    content: @Composable (ColorScheme) -> Unit,
) {
    val (lightColorScheme, darkColorScheme) =
        if (state.theme.isHighContrast) lightThemeHighContrast to darkThemeHighContrast else lightTheme to darkTheme

    val colorScheme = when (state.theme.mode) {
        ThemeMode.SYSTEM -> systemTheme(lightColorScheme, darkColorScheme)
        ThemeMode.LIGHT -> lightColorScheme
        ThemeMode.DARK -> darkColorScheme
    }

    CompositionLocalProvider(
        LocalAppTheme provides state.theme,
    ) {
        content(colorScheme)
    }
}
