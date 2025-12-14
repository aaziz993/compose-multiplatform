package presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.data.permission.PermissionsState
import clib.data.permission.rememberPermissionsState
import clib.data.share.rememberShare
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.auth.AuthState
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
import klib.data.cache.Cache
import klib.data.cache.CoroutineCache
import klib.data.location.locale.LocaleService
import clib.data.type.primitives.string.stringResource
import clib.presentation.appbar.AppBarState
import clib.presentation.appbar.rememberAppBarState
import clib.presentation.connectivity.ConnectivityState
import clib.presentation.connectivity.rememberConnectivityState
import clib.presentation.navigation.NavRoute
import klib.data.share.Share
import ui.navigation.presentation.App

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    config: Config = koinInject(),
    cache: Cache<String, Any> = koinInject(),
    coroutineCache: CoroutineCache<String, Any> = koinInject(),
    share: Share = rememberShare(),
    connectivity: Connectivity.Status = rememberConnectivity(koinInject()),
    stateStore: StateStore = rememberStateStore(),
    eventBus: EventBus = remember { EventBus() },
    appBarState: AppBarState = rememberAppBarState(config.ui.appBar),
    connectivityState: ConnectivityState = rememberConnectivityState(config.ui.connectivity),
    themeState: ThemeState = rememberThemeState(config.ui.theme),
    densityState: DensityState = rememberDensityState(config.ui.density),
    localeState: LocaleState = rememberLocaleState(config.localization.locale),
    localeService: LocaleService = koinInject(),
    authState: AuthState = koinInject(),
    permissionsState: PermissionsState = rememberPermissionsState(),
    onlineText: String = stringResource(Res.string.online),
    offlineText: String = stringResource(Res.string.offline),
    routes: Routes = App,
    routerFactory: @Composable (Routes) -> Router = { routes -> rememberRouter(routes) },
    navigatorFactory: @Composable (Routes) -> Navigator = {
        rememberNav3Navigator(
            it,
            if (it == routes) routes.routes.find { route -> route.name == config.ui.startRoute } as? NavRoute else null,
            authState.value,
            routes.find { route -> route.name == config.ui.authRoute } as? NavRoute,
            routes.find { route -> route.name == config.ui.authRedirectRoute } as? NavRoute,
        )
    },
): Unit = AppEnvironment(
    config,
    cache,
    coroutineCache,
    share,
    connectivity,
    stateStore,
    eventBus,
    appBarState,
    connectivityState,
    themeState,
    densityState,
    localeState,
    localeService,
    authState,
    permissionsState,
    onlineText,
    offlineText,
    routes,
    routerFactory,
    navigatorFactory,
)

@Preview
@Composable
private fun PreviewAppComposable(): Unit = AppComposable()
