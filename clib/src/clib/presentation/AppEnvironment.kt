package clib.presentation

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import clib.data.location.locale.LocalAppLocale
import clib.data.location.locale.customAppLocale
import clib.presentation.theme.AppTheme
import clib.presentation.theme.LocalAppDensity
import clib.presentation.theme.ThemeState
import clib.presentation.theme.customAppDensity
import clib.presentation.theme.darkColorScheme
import clib.presentation.theme.darkColorSchemeHighContrast
import clib.presentation.theme.lightColorScheme
import clib.presentation.theme.lightColorSchemeHighContrast

@Composable
public fun AppEnvironment(
    themeState: ThemeState = ThemeState(),
    lightTheme: ColorScheme = lightColorScheme,
    lightThemeHighContrast: ColorScheme = lightColorSchemeHighContrast,
    darkTheme: ColorScheme = darkColorScheme,
    darkThemeHighContrast: ColorScheme = darkColorSchemeHighContrast,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
): Unit = AppTheme(
    themeState,
    lightTheme,
    lightThemeHighContrast,
    darkTheme,
    darkThemeHighContrast,
) { colorScheme ->
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
    ) {
        CompositionLocalProvider(
            LocalAppLocale provides customAppLocale,
            LocalAppDensity provides customAppDensity,
        ) {
            key(customAppLocale, customAppDensity) {
                content()
            }
        }
    }
}


