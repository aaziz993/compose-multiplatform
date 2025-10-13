@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
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
import clib.presentation.auth.LocalAuth
import clib.presentation.components.navigation.AdvancedNavHost
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.components.navigation.Navigator
import clib.presentation.stateholders.BooleanStateHolder
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import klib.data.type.primitives.string.uppercaseFirstChar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
public fun NavScreen(
    modifier: Modifier = Modifier,
    startDestination: Destination = AuthRoute,
    navigator: Navigator<Destination> = koinInject(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    val auth = LocalAuth.current
    val drawerStateHolder: BooleanStateHolder = koinInject(named("drawer"))
    val isDrawerOpen by drawerStateHolder.state.collectAsStateWithLifecycle()

    AdvancedNavigationSuiteScaffold(
        route = NavRoute,
        startDestination = startDestination,
        navigator = navigator,
        navigationSuiteRoute = { destination, route ->
            route.item(
                auth = auth,
                transform = { label ->
                    Res.allStringResources[label]?.let { stringResource -> stringResource(stringResource) }
                        ?: label.uppercaseFirstChar()
                },
                destination = destination,
            ) { destination ->
                navigator.navigate(destination as Destination)
            }
        },
        modifier = modifier,
        navController = navController,
        onNavHostReady = onNavHostReady,
        layoutType = { currentDestination ->
            if (NavRoute.find(currentDestination) in listOf(PhoneCheckUp, Otp, Login)) NavigationSuiteType.None
            else with(currentWindowAdaptiveInfo()) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                    if (isDrawerOpen) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
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
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()

