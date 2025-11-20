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
import clib.presentation.navigation.LocalRouter
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.exception.NavigationException
import clib.presentation.navigation.rememberRouter
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
    motionScheme: MotionScheme = MotionScheme.expressive(),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = rememberAuthState(),
    router: Router = rememberRouter(),
    routes: Routes,
    authRoute: NavRoute? = null,
    authRedirectRoute: NavRoute? = null,
    onBack: (() -> Unit)? = null,
    onError: ((NavigationException) -> Unit)? = null,
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
            LocalRouter provides router,
            LocalAppDensity provides customAppDensity,
        ) {
            routes.Content(
                router,
                authState.auth,
                authRoute,
                authRedirectRoute,
                onBack,
                onError,
            )
        }
    }
}


