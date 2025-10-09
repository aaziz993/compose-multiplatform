@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.auth.viewmodel.AbstractAuthViewModel
import clib.presentation.components.connectivity.ConnectivityGlobalSnackbar
import clib.presentation.components.navigation.AdvancedNavHost
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.components.navigation.Navigator
import clib.presentation.components.navigation.model.Route
import clib.presentation.components.navigation.viewmodel.NavigationAction
import klib.data.type.primitives.string.uppercaseFirstChar
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
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
    authViewModel: AbstractAuthViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    val navState by navViewModel.state.collectAsStateWithLifecycle()
    val auth by authViewModel.state.collectAsStateWithLifecycle()

    AdvancedNavigationSuiteScaffold(
        route = NavRoute,
        startDestination = if (auth.user == null) startDestination else loggedInDestination,
        navigator = navigator,
        navigationSuiteRoute = { currentDestination, route ->
            route.item(
                text = { label, _ -> Text(text = label.uppercaseFirstChar()) },
                currentDestination = currentDestination,
            ) { destination ->
                navigatorViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination))
            }
        },
        navController = navController,
        onNavHostReady = onNavHostReady,
        layoutType = { currentDestination ->
            // Check if part of auth route.
            if (AuthRoute.find(currentDestination) != null) NavigationSuiteType.None
            else with(currentWindowAdaptiveInfo()) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                    if (navState.drawerOpen) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
                }
                else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
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

