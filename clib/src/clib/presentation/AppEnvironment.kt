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
import clib.presentation.theme.density.LocalAppDensity
import clib.presentation.theme.density.customAppDensity
import clib.presentation.theme.AppTheme
import clib.presentation.theme.darkColorScheme
import clib.presentation.theme.darkColorSchemeHighContrast
import clib.presentation.theme.lightColorScheme
import clib.presentation.theme.lightColorSchemeHighContrast
import clib.presentation.theme.model.Theme

@Composable
public fun AppEnvironment(
    theme: Theme,
    lightTheme: ColorScheme = lightColorScheme,
    lightThemeHighContrast: ColorScheme = lightColorSchemeHighContrast,
    darkTheme: ColorScheme = darkColorScheme,
    darkThemeHighContrast: ColorScheme = darkColorSchemeHighContrast,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
): Unit = AppTheme(
    theme,
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


