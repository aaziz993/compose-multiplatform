package presentation

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.data.permission.PermissionsState
import clib.data.permission.rememberPermissionsState
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.auth.AuthState
import clib.presentation.components.ComponentsState
import clib.presentation.components.rememberComponentsState
import clib.presentation.config.Config
import clib.presentation.connectivity.rememberConnectivity
import clib.presentation.event.EventBus
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.rememberRouter
import clib.presentation.state.StateStore
import clib.presentation.state.rememberStateStore
import clib.presentation.theme.ThemeState
import clib.presentation.theme.density.DensityState
import clib.presentation.theme.density.rememberDensityState
import clib.presentation.theme.rememberThemeState
import compose_app.generated.resources.Res
import compose_app.generated.resources.offline
import compose_app.generated.resources.online
import dev.jordond.connectivity.Connectivity
import org.jetbrains.compose.resources.stringResource
import ui.navigation.presentation.App
import ui.navigation.presentation.Auth
import ui.navigation.presentation.Services

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    config: Config = koinInject(),
    connectivity: Connectivity.Status = rememberConnectivity(koinInject()),
    onlineText: String = stringResource(Res.string.online),
    offlineText: String = stringResource(Res.string.offline),
    permissionsState: PermissionsState = rememberPermissionsState(),
    componentsState: ComponentsState = rememberComponentsState(config.ui.components),
    themeState: ThemeState = rememberThemeState(config.ui.theme),
    densityState: DensityState = rememberDensityState(config.ui.density),
    localeState: LocaleState = rememberLocaleState(config.ui.locale),
    authState: AuthState = koinInject(),
    stateStore: StateStore = rememberStateStore(),
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
        connectivity,
        onlineText,
        offlineText,
        permissionsState,
        componentsState,
        themeState,
        densityState,
        localeState,
        authState,
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
