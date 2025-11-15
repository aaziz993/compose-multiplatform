@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import clib.presentation.theme.model.ThemeMode

public val LocalThemeState: ProvidableCompositionLocal<ThemeState> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalThemeStateHolder") }

@Composable
public fun AppTheme(
    themeState: ThemeState = rememberThemeState(),
    lightColorScheme: ColorScheme = LightColors,
    lightColorSchemeHighContrast: ColorScheme = LightColorsHighContrast,
    darkColorScheme: ColorScheme = DarkColors,
    darkColorSchemeHighContrast: ColorScheme = DarkColorsHighContrast,
    content: @Composable (ColorScheme) -> Unit,
) {
    val (light, dark) =
        if (themeState.theme.isHighContrast) lightColorSchemeHighContrast to darkColorSchemeHighContrast else lightColorScheme to darkColorScheme

    val colorScheme = when (themeState.theme.mode) {
        ThemeMode.SYSTEM -> systemTheme(light, dark)
        ThemeMode.LIGHT -> light
        ThemeMode.DARK -> dark
    }

    CompositionLocalProvider(
        LocalThemeState provides themeState,
    ) {
        content(colorScheme)
    }
}
