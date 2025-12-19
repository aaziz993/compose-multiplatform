package presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import clib.data.permission.PermissionsState
import clib.data.permission.rememberPermissionsState
import clib.data.share.rememberShare
import clib.data.type.primitives.string.stringResource
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.appbar.AppBarState
import clib.presentation.appbar.rememberAppBarState
import clib.presentation.auth.AuthState
import clib.presentation.config.Config
import clib.presentation.connectivity.ConnectivityState
import clib.presentation.connectivity.rememberConnectivity
import clib.presentation.connectivity.rememberConnectivityState
import clib.presentation.event.EventBus
import clib.presentation.event.alert.GlobalAlertDialog
import clib.presentation.event.snackbar.GlobalSnackbar
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.RoutesState
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.rememberRoutesState
import clib.presentation.state.StateStore
import clib.presentation.state.rememberStateStore
import clib.presentation.theme.ThemeState
import clib.presentation.theme.density.DensityState
import clib.presentation.theme.density.rememberDensityState
import clib.presentation.theme.rememberThemeState
import compose_app.generated.resources.Res
import compose_app.generated.resources.close
import compose_app.generated.resources.confirm
import compose_app.generated.resources.offline
import compose_app.generated.resources.online
import dev.jordond.connectivity.Connectivity
import io.ktor.http.Url
import klib.data.cache.Cache
import klib.data.cache.CoroutineCache
import klib.data.location.locale.LocaleService
import klib.data.share.Share
import ui.navigation.presentation.Application

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    config: Config = koinInject(),
    cache: Cache<String, Any> = koinInject(),
    coroutineCache: CoroutineCache<String, Any> = koinInject(),
    share: Share = rememberShare(),
    connectivityStatus: Connectivity.Status = rememberConnectivity(koinInject()),
    onlineText: String = stringResource(Res.string.online),
    offlineText: String = stringResource(Res.string.offline),
    stateStore: StateStore = rememberStateStore(),
    eventBus: EventBus = remember { EventBus() },
    appBarState: AppBarState = rememberAppBarState(config.ui.appBar),
    connectivityState: ConnectivityState = rememberConnectivityState(config.ui.connectivity),
    routesState: RoutesState = rememberRoutesState(config.ui.routes),
    themeState: ThemeState = rememberThemeState(config.ui.theme),
    densityState: DensityState = rememberDensityState(config.ui.density),
    localeState: LocaleState = rememberLocaleState(config.localization.locale),
    localeService: LocaleService = koinInject(),
    authState: AuthState = koinInject(),
    permissionsState: PermissionsState = rememberPermissionsState(),
    routes: Routes = Application,
    routerFactory: @Composable (Routes) -> Router = {
        val isRoot = it == routes
        if (isRoot && config.ui.startRoute != null) remember { Router(it, config.ui.startRoute!!) }
        else remember { Router(it) }
    },
    navigatorFactory: @Composable (Routes) -> Navigator = {
        rememberNav3Navigator(
            routes = it,
            authRoute = config.ui.authRoute?.let(routes::resolve)?.lastOrNull(),
            authRedirectRoute = config.ui.authRedirectRoute?.let(routes::resolve)?.lastOrNull(),
        )
    },
    onDeepLink: Router.(Url) -> Unit = Router::push,
): Unit = AppEnvironment(
    config,
    cache,
    coroutineCache,
    share,
    connectivityStatus,
    onlineText,
    offlineText,
    stateStore,
    eventBus,
    appBarState,
    connectivityState,
    routesState,
    themeState,
    densityState,
    localeState,
    localeService,
    authState,
    permissionsState,
    routes,
    routerFactory,
    navigatorFactory,
    onDeepLink,
) {
    GlobalAlertDialog(
        { action ->
            IconButton(action) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(Res.string.confirm),
                    tint = Color.Green,
                )
            }
        },
        Modifier.align(Alignment.Center),
        { dismiss ->
            IconButton(dismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.close),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
    GlobalSnackbar(Modifier.align(Alignment.Center))
}

@Preview
@Composable
private fun PreviewAppComposable(): Unit = AppComposable()
