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
import clib.data.location.locale.stringResource
import clib.presentation.components.connectivity.ConnectivityGlobalSnackbar
import clib.presentation.components.navigation.AdvancedNavHost
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.components.navigation.Navigator
import clib.presentation.components.navigation.model.NavigationDestination
import clib.presentation.components.navigation.viewmodel.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import klib.data.type.primitives.string.uppercaseFirstChar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ui.auth.login.presentation.viewmodel.LoginViewModel
import ui.navigation.presentation.viewmodel.NavViewModel

@Suppress("ComposeModifierMissing")
@Composable
public fun NavScreen(
    navViewModel: NavViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel(),
    startDestination: NavigationDestination<*> = Login,
    loggedInDestination: NavigationDestination<*> = Login,
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {

    val loginState by loginViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(loginState.user) {
        if (loginState.user == null)
            navViewModel.action(NavigationAction.TypeSafeNavigation.NavigateAndClearCurrent(startDestination))
        else navViewModel.action(NavigationAction.TypeSafeNavigation.NavigateAndClearCurrent(loggedInDestination))
    }

    AdvancedNavigationSuiteScaffold(
        route = NavRoute,
        startDestination = startDestination,
        navigationSuiteRoute = { currentDestination, route ->
            route.item(
                text = { label, _ -> Text(text = label) },
                currentDestination = currentDestination,
            ) { destination ->
                navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination))
            }
        },
        navigator = koinInject<Navigator<Destination>>(),
        navController = navController,
        onNavHostReady = onNavHostReady,

        layoutType = with(currentWindowAdaptiveInfo()) {
            if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                if (true) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
            }
            else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                currentWindowAdaptiveInfo(),
            )
        },
    ) {
        AdvancedNavHost(
            navController,
            NavRoute,
            startDestination,
        ) { route ->
            route.item { action -> navViewModel.action(action) }
        }
    }

    ConnectivityGlobalSnackbar(koinInject())
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()

