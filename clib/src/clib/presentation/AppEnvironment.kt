package clib.presentation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import clib.data.permission.LocalPermissionsState
import clib.data.permission.PermissionsState
import clib.data.permission.rememberPermissionsState
import clib.data.share.LocalShare
import clib.data.share.rememberShare
import clib.presentation.appbar.AppBarState
import clib.presentation.appbar.LocalAppBarState
import clib.presentation.appbar.rememberAppBarState
import clib.presentation.auth.AuthState
import clib.presentation.auth.LocalAuthState
import clib.presentation.auth.rememberAuthState
import clib.presentation.cache.LocalCache
import clib.presentation.cache.LocalCoroutineCache
import clib.presentation.config.Config
import clib.presentation.config.LocalConfig
import clib.presentation.connectivity.ConnectivityState
import clib.presentation.connectivity.LocalConnectivityState
import clib.presentation.connectivity.LocalConnectivityStatus
import clib.presentation.connectivity.rememberConnectivity
import clib.presentation.connectivity.rememberConnectivityState
import clib.presentation.events.EventBus
import clib.presentation.events.LocalEventBus
import clib.presentation.events.alert.GlobalAlertDialog
import clib.presentation.events.alert.GlobalAlertEventController
import clib.presentation.events.alert.model.AlertEvent
import clib.presentation.events.snackbar.GlobalSnackbar
import clib.presentation.events.snackbar.GlobalSnackbarEventController
import clib.presentation.events.snackbar.model.SnackbarEvent
import clib.presentation.locale.LocalAppLocale
import clib.presentation.locale.LocalLocaleState
import clib.presentation.locale.LocalLocalization
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.locale.rememberLocalization
import clib.presentation.navigation.LocalRoutesState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.RoutesState
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.rememberRoutesState
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
import com.sunildhiman90.kmauth.core.KMAuthConfig
import com.sunildhiman90.kmauth.core.KMAuthInitializer
import com.sunildhiman90.kmauth.supabase.KMAuthSupabase
import dev.jordond.connectivity.Connectivity.Status
import io.ktor.http.Url
import klib.data.cache.Cache
import klib.data.cache.CoroutineCache
import klib.data.cache.emptyCache
import klib.data.cache.emptyCoroutineCache
import klib.data.location.locale.LocaleService
import klib.data.net.createConnectivity
import klib.data.share.Share
import kotlin.String
import kotlinx.coroutines.MainScope

@OptIn(ExperimentalFoundationApi::class)
@Suppress("ComposeParameterOrder", "ComposeModifierMissing")
@Composable
public fun AppEnvironment(
    config: Config = Config(),
    cache: Cache<String, Any> = emptyCache(),
    coroutineCache: CoroutineCache<String, Any> = emptyCoroutineCache(),
    share: Share = rememberShare(),
    connectivityStatus: Status = rememberConnectivity(createConnectivity(MainScope())),
    onlineText: String = "Online",
    offlineText: String = "Offline",
    stateStore: StateStore = rememberStateStore(),
    eventBus: EventBus = remember { EventBus() },
    appBarState: AppBarState = rememberAppBarState(config.ui.appBar),
    connectivityState: ConnectivityState = rememberConnectivityState(config.ui.connectivity),
    routesState: RoutesState = rememberRoutesState(config.ui.routes),
    themeState: ThemeState = rememberThemeState(config.ui.theme),
    densityState: DensityState = rememberDensityState(config.ui.density),
    localeState: LocaleState = rememberLocaleState(config.localization.locale),
    localeService: LocaleService = LocaleService(),
    authState: AuthState = rememberAuthState(),
    permissionsState: PermissionsState = rememberPermissionsState(),
    routes: Routes,
    routerFactory: @Composable (Routes) -> Router = {
        val isRoot = it == routes
        if (isRoot && config.ui.startRoute != null) remember { Router(it, config.ui.startRoute) }
        else remember { Router(it) }
    },
    navigatorFactory: @Composable (Routes) -> Navigator = {
        val isRoot = it == routes
        rememberNav3Navigator(
            routes = it,
            authRoute = config.ui.authRoute?.let(routes::resolve)?.lastOrNull(),
            authRedirectRoute = if (isRoot) config.ui.authRedirectRoute?.let(routes::resolve)?.lastOrNull() else null,
        )
    },
    onDeepLink: Router.(Url) -> Unit = Router::push,
    content: @Composable BoxScope.() -> Unit = {
        GlobalAlertDialog(modifier = Modifier.align(Alignment.Center))
        GlobalSnackbar(Modifier.align(Alignment.Center))
    }
) {
    ComposeFoundationFlags.isNewContextMenuEnabled = true
    config.log.configure()
    config.auth.google?.let { google ->
        KMAuthInitializer.initialize(KMAuthConfig.forGoogle(webClientId = google.webClientId, clientSecret = google.clientSecret))
    }
    config.auth.supabases.forEach { supabase ->
        KMAuthSupabase.initialize(
            KMAuthConfig.forSupabase(
                supabaseUrl = supabase.supabaseUrl,
                supabaseKey = supabase.supabaseKey,
                autoLoadFromStorage = supabase.autoLoadFromStorage,
                autoRefreshToken = supabase.autoRefreshToken,
                deepLinkHost = supabase.deepLinkHost,
                deepLinkScheme = supabase.deepLinkScheme,
                kmAuthSupabaseFlowType = supabase.kmAuthSupabaseFlowType,
            ),
        )
    }

    val localization by rememberLocalization(localeState, localeService)

    remember(routesState) {
        routes.forEach(routesState::configure)
    }

    CompositionLocalProvider(
        LocalConfig provides config,
        LocalCache provides cache,
        LocalCoroutineCache provides coroutineCache,
        LocalShare provides share,
        LocalConnectivityStatus provides connectivityStatus,
        LocalRoutesState provides routesState,
        LocalStateStore provides stateStore,
        LocalEventBus provides eventBus,
        LocalAppBarState provides appBarState,
        LocalConnectivityState provides connectivityState,
        LocalThemeState provides themeState,
        LocalAppTheme provides themeState.value.isDark(),
        LocalDensityState provides densityState,
        LocalDensity provides densityState.value,
        LocalLocaleState provides localeState,
        LocalAppLocale provides localeState.value,
        LocalLocalization provides localization,
        LocalAuthState provides authState,
        LocalPermissionsState provides permissionsState,
    ) {
        val theme = themeState.value

        LaunchedEffect(connectivityStatus) {
            with(connectivityState.value) {
                if (isConnectivityAlert)
                    when (connectivityStatus) {
                        is Status.Connected -> GlobalAlertEventController.sendEvent(
                            AlertEvent(text = { Text(onlineText) }),
                        )

                        is Status.Disconnected -> GlobalAlertEventController.sendEvent(
                            AlertEvent(text = { Text(offlineText) }),
                        )
                    }

                if (isConnectivitySnackbar)
                    when (connectivityStatus) {
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
            val dynamicColorScheme = theme.currentDynamicColorScheme

            val state = rememberDynamicMaterialThemeState(
                seedColor = dynamicColorScheme.seedColor,
                isDark = theme.isDark(),
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
            (if (theme.animate)
                animateColorScheme(
                    colorScheme = colorScheme,
                    animationSpec = {
                        theme.animationSpec as FiniteAnimationSpec<Color>
                    },
                )
            else colorScheme) to state.seedColor
        }
        else {
            val colorScheme = theme.currentColorScheme
            (if (theme.animate)
                animateColorScheme(
                    colorScheme = colorScheme,
                    animationSpec = {
                        theme.animationSpec as FiniteAnimationSpec<Color>
                    },
                )
            else colorScheme) to Color.Transparent
        }

        CompositionLocalProvider(LocalDynamicMaterialThemeSeed provides seedColor) {
            MaterialExpressiveTheme(
                colorScheme,
                if (theme.isExpressive) MotionScheme.expressive() else MotionScheme.standard(),
                theme.shapes,
                theme.typography,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    routes.Nav3Host(routerFactory, navigatorFactory)
                    content()
                }
            }
        }
    }
}


