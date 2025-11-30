package clib.presentation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import clib.presentation.auth.AuthState
import clib.presentation.auth.LocalAuthState
import clib.presentation.auth.rememberAuthState
import clib.presentation.event.EventBus
import clib.presentation.event.LocalEventBus
import clib.presentation.locale.LocalAppLocale
import clib.presentation.locale.LocalLocaleState
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.rememberRouter
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.state.LocalStateStore
import clib.presentation.state.StateStore
import clib.presentation.state.rememberStateStore
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.LocalThemeState
import clib.presentation.theme.ThemeState
import clib.presentation.theme.density.DensityState
import clib.presentation.theme.density.LocalDensityState
import clib.presentation.theme.density.rememberDensityState
import clib.presentation.theme.rememberThemeState
import com.materialkolor.LocalDynamicMaterialThemeSeed
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.ktx.animateColorScheme
import com.materialkolor.rememberDynamicMaterialThemeState

@Suppress("ComposeParameterOrder", "ComposeModifierMissing")
@Composable
public fun AppEnvironment(
    themeState: ThemeState = rememberThemeState(),
    densityState: DensityState = rememberDensityState(),
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = rememberAuthState(),
    stateStore: StateStore = rememberStateStore(
        mapOf(
            QuickAccess::class.toString() to mutableStateOf(QuickAccess()),
        ),
    ),
    eventBus: EventBus = remember { EventBus() },
    routerFactory: @Composable (Routes) -> Router = { routes -> rememberRouter(routes) },
    navigatorFactory: @Composable (Routes) -> Navigator = { routes -> rememberNav3Navigator(routes) },
    routes: Routes,
): Unit = CompositionLocalProvider(
    LocalThemeState provides themeState,
    LocalAppTheme provides themeState.theme.isDark,
    LocalDensityState provides densityState,
    LocalDensity provides densityState.density,
    LocalLocaleState provides localeState,
    LocalAppLocale provides localeState.locale,
    LocalAuthState provides authState,
    LocalStateStore provides stateStore,
    LocalEventBus provides eventBus,
) {
    val theme = themeState.theme

    val (colorScheme, seedColor) = if (theme.isDynamic) {
        val dynamicColorPalette =
            if (theme.isHighContrast) theme.dynamicColorPaletteHighContrast else theme.dynamicColorPalette

        val state = rememberDynamicMaterialThemeState(
            seedColor = dynamicColorPalette!!.seedColor,
            isDark = isSystemInDarkTheme(),
            isAmoled = dynamicColorPalette.isAmoled,
            primary = dynamicColorPalette.primary,
            secondary = dynamicColorPalette.secondary,
            tertiary = dynamicColorPalette.tertiary,
            neutral = dynamicColorPalette.neutral,
            neutralVariant = dynamicColorPalette.neutralVariant,
            error = dynamicColorPalette.error,
            contrastLevel = dynamicColorPalette.contrastLevel,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
            platform = dynamicColorPalette.platform,
        )

        Surface { }

        val colorScheme = state.colorScheme
        (if (!dynamicColorPalette.animate) colorScheme
        else animateColorScheme(
            colorScheme = colorScheme,
            animationSpec = {
                dynamicColorPalette.animationSpec as FiniteAnimationSpec<Color>
            },
        )) to state.seedColor
    }
    else {
        val colorPalette =
            if (theme.isHighContrast) theme.colorPaletteHighContrast else theme.colorPalette

        (if (isSystemInDarkTheme()) colorPalette.darkColorScheme
        else colorPalette.lightColorScheme) to Color.Transparent
    }

    CompositionLocalProvider(LocalDynamicMaterialThemeSeed provides seedColor) {
        MaterialExpressiveTheme(
            colorScheme,
            if (theme.isExpressive) MotionScheme.expressive() else MotionScheme.standard(),
            theme.shapes,
            theme.typography,
        ) {
            routes.Nav3Host(routerFactory, navigatorFactory)
        }
    }
}


