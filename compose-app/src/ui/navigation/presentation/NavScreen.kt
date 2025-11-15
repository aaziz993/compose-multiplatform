package ui.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavBackStack
import androidx.window.core.layout.WindowSizeClass
import clib.data.type.primitives.string.asStringResource
import clib.di.koinInject
import clib.presentation.auth.AuthState
import clib.presentation.components.connectivity.Connectivity
import clib.presentation.event.alert.GlobalAlertDialog
import clib.presentation.event.snackbar.GlobalSnackbar
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.locale.LocaleState
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Router
import clib.presentation.navigation.rememberNavBackStack
import clib.presentation.navigation.rememberRouter
import clib.presentation.theme.ThemeState
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import presentation.components.scaffold.AppBar
import compose_app.generated.resources.online
import compose_app.generated.resources.offline
import org.jetbrains.compose.resources.getString

@Suppress("UNCHECKED_CAST")
@Composable
public fun NavScreen(
    modifier: Modifier = Modifier,
    themeState: ThemeState = ThemeState(),
    localeState: LocaleState = LocaleState(),
    authState: AuthState = AuthState(),
    authRoute: NavRoute = Auth,
    publicRoute: NavRoute = authRoute,
    protectedRoute: NavRoute = Home,
    backStack: NavBackStack<NavRoute> = App.rememberNavBackStack(authRoute),
    router: Router = rememberRouter(),
) {
    val currentRoute = router.currentRoute ?: return
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

    AppBar(
        modifier = modifier,
        title = { Text(text = currentRoute.name.asStringResource(Res.allStringResources)) },
        theme = themeState.theme,
        onThemeChange = { theme -> themeState.theme = theme },
        locale = localeState.locale,
        onLocaleChange = { locale -> localeState.locale = locale },
        auth = authState.auth,
        onAuthChange = { auth -> authState.auth = auth },
        isDrawerOpen = navigationSuiteScaffoldState.currentValue == NavigationSuiteScaffoldValue.Visible,
        onDrawerToggle = navigationSuiteScaffoldState::toggle,
        hasBackRoute = router.hasBackRoute,
        onNavigationAction = router::actions,
    ) { innerPadding ->
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                App.navigationItem(
                    currentRoute = currentRoute,
                    onNavigate = { route -> router.push(route) },
                    auth = authState.auth,
                    ::item,
                )
            },
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            layoutType = if (App.isNavigationItem(authState.auth))
                with(currentWindowAdaptiveInfo()) {
                    if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                        NavigationSuiteType.NavigationDrawer
                    else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(this)
                }
            else NavigationSuiteType.None,
            state = navigationSuiteScaffoldState,
        ) {
            GlobalSnackbar()

            GlobalAlertDialog()

            Connectivity(
                koinInject(),
                {
                    GlobalSnackbarEventController.sendEvent(
                        SnackbarEvent(getString(Res.string.online)),
                    )
                },
                {
                    GlobalSnackbarEventController.sendEvent(
                        SnackbarEvent(getString(Res.string.offline)),
                    )
                },
            )

            App.Content(backStack, router::pop)
        }
    }
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()
