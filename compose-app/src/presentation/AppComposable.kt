package presentation

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.auth.AuthState
import clib.presentation.config.Config
import clib.presentation.event.EventBus
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.rememberRouter
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.state.StateStore
import clib.presentation.state.rememberStateStore
import clib.presentation.theme.ThemeState
import clib.presentation.theme.density.DensityState
import clib.presentation.theme.density.rememberDensityState
import clib.presentation.theme.model.DynamicColorPalette
import clib.presentation.theme.model.Theme
import clib.presentation.theme.rememberThemeState
import dev.jordond.connectivity.Connectivity
import presentation.theme.SquircleShapes
import presentation.theme.Typography
import presentation.theme.colorPalette
import presentation.theme.colorPaletteHighContrast
import ui.navigation.presentation.App
import ui.navigation.presentation.Auth
import ui.navigation.presentation.Services

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    config: Config = koinInject(),
    themeState: ThemeState = rememberThemeState(
        Theme(
            colorPalette = colorPalette,
            colorPaletteHighContrast = colorPaletteHighContrast,
            dynamicColorPalette = DynamicColorPalette(
                seedColor = Color.Cyan,
                animate = true,
                animationSpec = spring(),
            ),
            dynamicColorPaletteHighContrast = DynamicColorPalette(
                seedColor = Color.Blue,
                animate = true,
                animationSpec = spring(),
            ),
            shapes = SquircleShapes,
            typography = Typography,
        ),
    ),
    densityState: DensityState = rememberDensityState(),
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = koinInject(),
    connectivity: Connectivity = koinInject(),
    stateStore: StateStore = rememberStateStore(
        mapOf(
            QuickAccess::class.toString() to mutableStateOf(QuickAccess()),
        ),
    ),
    eventBus: EventBus = remember { EventBus() },
    routes: Routes = App,
    routerFactory: @Composable (Routes) -> Router = { routes -> rememberRouter(routes) },
    navigatorFactory: @Composable (Routes) -> Navigator = {
        rememberNav3Navigator(
            it,
            authState.auth,
            Auth,
            if (it == routes) Services else null,
        )
    },
) {
    ComposeFoundationFlags.isNewContextMenuEnabled = true
    AppEnvironment(
        config,
        themeState,
        densityState,
        localeState,
        authState,
        connectivity,
        stateStore,
        eventBus,
        routerFactory,
        navigatorFactory,
        routes,
    )
}

@Preview
@Composable
public fun PreviewAppComposable(): Unit = AppComposable()
