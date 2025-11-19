package clib.presentation

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import clib.presentation.auth.AuthState
import clib.presentation.auth.LocalAuthState
import clib.presentation.auth.rememberAuthState
import clib.presentation.locale.LocalLocaleState
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.theme.AppTheme
import clib.presentation.theme.DarkColors
import clib.presentation.theme.DarkColorsHighContrast
import clib.presentation.theme.LightColors
import clib.presentation.theme.LightColorsHighContrast
import clib.presentation.theme.ThemeState
import clib.presentation.theme.density.LocalAppDensity
import clib.presentation.theme.density.customAppDensity
import clib.presentation.theme.rememberThemeState

@Suppress("ComposeParameterOrder")
@Composable
public fun AppEnvironment(
    themeState: ThemeState = rememberThemeState(),
    lightColorScheme: ColorScheme = LightColors,
    lightColorSchemeHighContrast: ColorScheme = LightColorsHighContrast,
    darkColorScheme: ColorScheme = DarkColors,
    darkColorSchemeHighContrast: ColorScheme = DarkColorsHighContrast,
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = rememberAuthState(),
    motionScheme: MotionScheme = MotionScheme.expressive(),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit,
): Unit = AppTheme(
    themeState,
    lightColorScheme,
    lightColorSchemeHighContrast,
    darkColorScheme,
    darkColorSchemeHighContrast,
) { colorScheme ->
    MaterialExpressiveTheme(
        colorScheme,
        motionScheme,
        shapes,
        typography,
    ) {
        CompositionLocalProvider(
            LocalLocaleState provides localeState,
            LocalAuthState provides authState,
            LocalAppDensity provides customAppDensity,
            content = content,
        )
    }
}


