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
import clib.presentation.auth.AuthState
import clib.presentation.auth.LocalAuthState
import clib.presentation.auth.rememberAuthState
import clib.presentation.density.DensityState
import clib.presentation.density.LocalAppDensity
import clib.presentation.density.rememberDensityState
import clib.presentation.locale.LocalLocaleState
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.rememberRouter
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.LocalThemeState
import clib.presentation.theme.ThemeState
import clib.presentation.theme.model.DynamicTheme
import clib.presentation.theme.model.Theme
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
    routerFactory: @Composable (Routes) -> Router = { routes -> rememberRouter(routes) },
    navigatorFactory: @Composable (Routes) -> Navigator = { routes -> rememberNav3Navigator(routes) },
    routes: Routes,
): Unit = CompositionLocalProvider(
    LocalThemeState provides themeState,
    LocalAppTheme provides themeState.theme.isDark,
    LocalLocaleState provides localeState,
    LocalAuthState provides authState,
    LocalAppDensity provides densityState.density,
) {
    val theme = themeState.theme
    when (theme) {
        is Theme -> MaterialExpressiveTheme(
            if (LocalAppTheme.current) theme.darkColorScheme?.toColorScheme()
            else theme.lightColorScheme?.toColorScheme(),
            motionScheme,
            shapes,
            typography,
        ) {
            routes.Nav3Host(routerFactory, navigatorFactory)
        }

        is DynamicTheme -> DynamicMaterialExpressiveTheme(
            seedColor = theme.seedColor.toColor(),
            motionScheme = motionScheme,
            isAmoled = theme.isAmoled,
            primary = theme.primary?.toColor(),
            secondary = theme.secondary?.toColor(),
            tertiary = theme.tertiary?.toColor(),
            neutral = theme.neutral?.toColor(),
            neutralVariant = theme.neutralVariant?.toColor(),
            error = theme.error?.toColor(),
            contrastLevel = theme.contrastLevel,
            platform = theme.platform,
            shapes = shapes ?: MaterialTheme.shapes,
            typography = typography ?: MaterialTheme.typography,
            animate = theme.animate,
            animationSpec = animationSpec,
        ) {
            routes.Nav3Host(routerFactory, navigatorFactory)
        }
    }
}


