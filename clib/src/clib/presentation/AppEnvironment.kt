package clib.presentation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import clib.data.permission.LocalPermissionsState
import clib.data.permission.PermissionsState
import clib.data.permission.rememberPermissionsState
import clib.presentation.auth.AuthState
import clib.presentation.auth.LocalAuthState
import clib.presentation.auth.rememberAuthState
import clib.presentation.components.ComponentsState
import clib.presentation.components.LocalComponentsState
import clib.presentation.components.rememberComponentsState
import clib.presentation.config.Config
import clib.presentation.config.LocalConfig
import clib.presentation.connectivity.LocalConnectivity
import clib.presentation.connectivity.rememberConnectivity
import clib.presentation.event.EventBus
import clib.presentation.event.LocalEventBus
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.locale.LocalAppLocale
import clib.presentation.locale.LocalLocaleState
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.rememberRouter
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
import dev.jordond.connectivity.Connectivity.Status
import klib.data.net.createConnectivity
import kotlinx.coroutines.MainScope

@Suppress("ComposeParameterOrder", "ComposeModifierMissing")
@Composable
public fun AppEnvironment(
    config: Config = Config(),
    connectivity: Status = rememberConnectivity(createConnectivity(MainScope())),
    onlineText: String = "Online",
    offlineText: String = "Offline",
    permissionsState: PermissionsState = rememberPermissionsState(),
    componentsState: ComponentsState = rememberComponentsState(config.ui.components),
    themeState: ThemeState = rememberThemeState(config.ui.theme),
    densityState: DensityState = rememberDensityState(config.ui.density),
    localeState: LocaleState = rememberLocaleState(config.localization.locale),
    authState: AuthState = rememberAuthState(),
    stateStore: StateStore = rememberStateStore(),
    eventBus: EventBus = remember { EventBus() },
    routerFactory: @Composable (Routes) -> Router = { routes -> rememberRouter(routes) },
    navigatorFactory: @Composable (Routes) -> Navigator = { routes -> rememberNav3Navigator(routes) },
    routes: Routes,
) {
    config.log.configureKmLogging()

    CompositionLocalProvider(
        LocalConfig provides config,
        LocalConnectivity provides connectivity,
        LocalPermissionsState provides permissionsState,
        LocalComponentsState provides componentsState,
        LocalThemeState provides themeState,
        LocalAppTheme provides themeState.theme.isDark,
        LocalDensityState provides densityState,
        LocalDensity provides densityState.density,
        LocalLocaleState provides localeState,
        LocalAppLocale provides localeState.locale,
        LocalAuthState provides authState,
        LocalConnectivity provides connectivity,
        LocalStateStore provides stateStore,
        LocalEventBus provides eventBus,
    ) {
        val theme = themeState.theme

        with(componentsState.components.connectivity) {
            LaunchedEffect(connectivity) {
                if (isConnectivityAlert)
                    when (connectivity) {
                        is Status.Connected -> GlobalAlertEventController.sendEvent(
                            AlertEvent(onlineText),
                        )

                        is Status.Disconnected -> GlobalAlertEventController.sendEvent(
                            AlertEvent(offlineText),
                        )
                    }

                if (isConnectivitySnackbar)
                    when (connectivity) {
                        is Status.Connected -> GlobalSnackbarEventController.sendEvent(
                            SnackbarEvent(onlineText),
                        )

                        is Status.Disconnected -> GlobalSnackbarEventController.sendEvent(
                            SnackbarEvent(offlineText),
                        )
                    }
            }
        }

        val (colorScheme, seedColor) = if (theme.isDynamic) {
            val dynamicColorScheme =
                if (theme.isHighContrast) theme.dynamicColorSchemeHighContrast else theme.dynamicColorScheme

            val state = rememberDynamicMaterialThemeState(
                seedColor = dynamicColorScheme.seedColor,
                isDark = isSystemInDarkTheme(),
                isAmoled = dynamicColorScheme.isAmoled,
                primary = dynamicColorScheme.primary,
                secondary = dynamicColorScheme.secondary,
                tertiary = dynamicColorScheme.tertiary,
                neutral = dynamicColorScheme.neutral,
                neutralVariant = dynamicColorScheme.neutralVariant,
                error = dynamicColorScheme.error,
                contrastLevel = dynamicColorScheme.contrastLevel,
                specVersion = ColorSpec.SpecVersion.SPEC_2025,
                platform = dynamicColorScheme.platform,
            )

            Surface { }

            val colorScheme = state.colorScheme
            (if (!dynamicColorScheme.animate) colorScheme
            else animateColorScheme(
                colorScheme = colorScheme,
                animationSpec = {
                    dynamicColorScheme.animationSpec as FiniteAnimationSpec<Color>
                },
            )) to state.seedColor
        }
        else {
            val (lightColorScheme, darkColorScheme) =
                if (theme.isHighContrast) theme.lightColorSchemeHighContrast to theme.darkColorSchemeHighContrast
                else theme.lightColorScheme to theme.darkColorScheme

            (if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme) to Color.Transparent
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
}


