package clib.presentation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import clib.di.koinInject
import clib.presentation.auth.AuthState
import clib.presentation.auth.LocalAuthState
import clib.presentation.auth.rememberAuthState
import clib.presentation.locale.LocalLocaleState
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.LocalThemeState
import clib.presentation.theme.ThemeState
import clib.presentation.theme.density.DensityState
import clib.presentation.theme.density.LocalAppDensity
import clib.presentation.theme.density.rememberDensityState
import clib.presentation.theme.model.DynamicColorPalette
import clib.presentation.theme.model.StaticColorPalette
import clib.presentation.theme.model.toColorScheme
import clib.presentation.theme.rememberThemeState
import com.materialkolor.DynamicMaterialExpressiveTheme

@Suppress("ComposeParameterOrder")
@Composable
public fun AppEnvironment(
    themeState: ThemeState = rememberThemeState(),
    densityState: DensityState = rememberDensityState(),
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = rememberAuthState(),
    motionScheme: MotionScheme? = null,
    shapes: Shapes? = null,
    typography: Typography? = null,
    animationSpec: FiniteAnimationSpec<Color> = spring(),
    routerFactory: @Composable (Routes) -> Router = { routes -> koinInject() },
    navigatorFactory: @Composable (Routes) -> Navigator = { routes -> rememberNav3Navigator(routes) },
    routes: Routes,
): Unit = CompositionLocalProvider(
    LocalThemeState provides themeState,
    LocalAppTheme provides themeState.theme.isDark,
    LocalLocaleState provides localeState,
    LocalAuthState provides authState,
    LocalAppDensity provides densityState.density?.toDensity(),
) {
    val theme = themeState.theme

    when (theme.colorPalette) {
        is StaticColorPalette -> MaterialExpressiveTheme(
            if (LocalAppTheme.current) theme.colorPalette.lightColorScheme?.toColorScheme()
            else theme.colorPalette.darkColorScheme?.toColorScheme(),
            motionScheme,
            shapes,
            typography,
        ) {
            routes.Nav3Host(routerFactory, navigatorFactory)
        }

        is DynamicColorPalette -> DynamicMaterialExpressiveTheme(
            seedColor = theme.colorPalette.seedColor.toColor(),
            motionScheme = motionScheme,
            isAmoled = theme.colorPalette.isAmoled,
            primary = theme.colorPalette.primary?.toColor(),
            secondary = theme.colorPalette.secondary?.toColor(),
            tertiary = theme.colorPalette.tertiary?.toColor(),
            neutral = theme.colorPalette.neutral?.toColor(),
            neutralVariant = theme.colorPalette.neutralVariant?.toColor(),
            error = theme.colorPalette.error?.toColor(),
            contrastLevel = theme.colorPalette.contrastLevel,
            platform = theme.colorPalette.platform,
            shapes = shapes ?: MaterialTheme.shapes,
            typography = typography ?: MaterialTheme.typography,
            animate = theme.colorPalette.animate,
            animationSpec = animationSpec,
        ) {
            routes.Nav3Host(routerFactory, navigatorFactory)
        }
    }
}


