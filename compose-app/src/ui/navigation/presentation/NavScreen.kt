@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.auth.LocalAppAuth
import clib.presentation.components.connectivity.ConnectivityGlobalSnackbar
import clib.presentation.components.navigation.AdvancedNavHost
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.components.navigation.Navigator
import clib.presentation.components.navigation.model.Route
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import klib.data.type.primitives.string.uppercaseFirstChar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ui.navigation.presentation.viewmodel.NavViewModel

@Suppress("ComposeModifierMissing")
@Composable
public fun NavScreen(
    startDestination: Route = Home,
    navigator: Navigator<Route> = koinInject(),
    navViewModel: NavViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    val navState by navViewModel.state.collectAsStateWithLifecycle()

    val auth = LocalAppAuth.current

    AdvancedNavigationSuiteScaffold(
        route = NavRoute,
        startDestination = startDestination,
        navigator = navigator,
        navigationSuiteRoute = { currentDestination, route ->
            route.item(
                auth = auth,
                text = { label, _ ->
                    Res.allStringResources[label]?.let { stringResource -> Text(text = stringResource(stringResource)) }
                        ?: Text(text = label.uppercaseFirstChar())
                },
                currentDestination = currentDestination,
            ) { destination ->
                navigator.navigate(destination)
            }
        },
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        onNavHostReady = onNavHostReady,
        layoutType = { currentDestination ->
            if (NavRoute.find(currentDestination) in listOf(PhoneCheckUp, Otp, Login)) NavigationSuiteType.None
            else with(currentWindowAdaptiveInfo()) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                    if (navState.isDrawerOpen) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
                }
                else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
            }
        },
    ) {
        AdvancedNavHost(
            navController = navController,
            route = NavRoute,
            startDestination = startDestination,
            modifier = Modifier,
        ) { route ->
            route.item { action -> navigator.navigate(action) }
        }
    }

    ConnectivityGlobalSnackbar(koinInject())
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()

