@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.components.connectivity.ConnectivityGlobalSnackbar
import clib.presentation.components.navigation.AdvancedNavHost
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.components.navigation.Navigator
import clib.presentation.components.navigation.model.Route
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ui.auth.login.presentation.viewmodel.LoginViewModel
import ui.navigation.presentation.viewmodel.NavViewModel
import ui.navigation.presentation.viewmodel.NavigatorViewModel

@Suppress("ComposeModifierMissing")
@Composable
public fun NavScreen(
    startDestination: Route = AuthRoute,
    loggedInDestination: Route = Services,
    navigator: Navigator<Destination> = koinInject(),
    navigatorViewModel: NavigatorViewModel = koinViewModel(),
    navViewModel: NavViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    val navState by navViewModel.state.collectAsStateWithLifecycle()
    val loginState by loginViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(loginState.user) {
        if (loginState.user == null)
            navigatorViewModel.action(NavigationAction.TypeSafeNavigation.NavigateAndClearCurrent(startDestination))
        else navigatorViewModel.action(NavigationAction.TypeSafeNavigation.NavigateAndClearCurrent(loggedInDestination))
    }

    AdvancedNavigationSuiteScaffold(
        route = NavRoute,
        startDestination = if (loginState.user == null) startDestination else loggedInDestination,
        navigator = navigator,
        navigationSuiteRoute = { currentDestination, route ->
            route.item(
                text = { label, _ -> Text(text = label) },
                currentDestination = currentDestination,
            ) { destination ->
                navigatorViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination))
            }
        },
        navController = navController,
        onNavHostReady = onNavHostReady,
        layoutType = { currentDestination ->
            if (NavRoute.current(currentDestination) != Login) NavigationSuiteType.None
            else with(currentWindowAdaptiveInfo()) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                    if (navState.drawerOpen) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
                }
                else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                    currentWindowAdaptiveInfo(),
                )
            }
        },
    ) {
        AdvancedNavHost(
            navController,
            NavRoute,
            startDestination,
        ) { route ->
            route.item { action -> navigatorViewModel.action(action) }
        }
    }

    ConnectivityGlobalSnackbar(koinInject())
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()

