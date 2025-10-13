package clib.presentation

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import clib.presentation.auth.LocalAuth
import clib.presentation.locale.LocalAppLocale
import clib.presentation.locale.customAppLocale
import clib.presentation.theme.AppTheme
import clib.presentation.theme.DarkColors
import clib.presentation.theme.DarkColorsHighContrast
import clib.presentation.theme.density.LocalAppDensity
import clib.presentation.theme.density.customAppDensity
import clib.presentation.theme.LightColors
import clib.presentation.theme.LightColorsHighContrast
import clib.presentation.theme.model.Theme
import klib.data.location.locale.Locale
import klib.data.type.auth.model.Auth

@Composable
public fun AppEnvironment(
    theme: Theme = Theme(),
    locale: Locale? = null,
    auth: Auth = Auth(),
    lightColorScheme: ColorScheme = LightColors,
    lightColorSchemeHighContrast: ColorScheme = LightColorsHighContrast,
    darkColorScheme: ColorScheme = DarkColors,
    darkColorSchemeHighContrast: ColorScheme = DarkColorsHighContrast,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
): Unit = AppTheme(
    theme,
    lightColorScheme,
    lightColorSchemeHighContrast,
    darkColorScheme,
    darkColorSchemeHighContrast,
) { colorScheme ->
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
    ) {
        val currentLocale = locale ?: customAppLocale

        CompositionLocalProvider(
            LocalAppLocale provides currentLocale,
            LocalAppDensity provides customAppDensity,
            LocalAuth provides auth,
        ) {
            key(currentLocale, customAppDensity, auth) {
                content()
            }
        }
    }
}


