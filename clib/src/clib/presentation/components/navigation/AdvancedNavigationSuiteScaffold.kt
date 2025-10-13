@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.model.Route
import clib.presentation.noLocalProvidedFor

public val LocalDestination: ProvidableCompositionLocal<NavDestination?> = staticCompositionLocalOf { noLocalProvidedFor("LocalDestination") }
public val LocalHasPreviousDestination: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { noLocalProvidedFor("LocalHasPreviousDestination") }
public val LocalDestinationTitle: ProvidableCompositionLocal<String> = staticCompositionLocalOf { noLocalProvidedFor("LocalDestinationTitle") }

@Composable
public fun <Dest : Any> AdvancedNavigationSuiteScaffold(
    route: NavigationRoute<Dest>,
    startDestination: Dest,
    navigator: Navigator<Dest>,
    navigationSuiteRoute: NavigationSuiteScope.(destination: NavDestination?, route: Route<Dest>) -> Unit,
    modifier: Modifier = Modifier,
    navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    layoutType: @Composable (destination: NavDestination?) -> NavigationSuiteType = {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    },
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination
    val hasPreviousDestination by remember(navBackStackEntry) {
        derivedStateOf { navController.previousBackStackEntry != null }
    }
    var destinationTitle: String by remember { mutableStateOf("") }

    route.single { route -> route.isDestination(LocalDestination.current) }.ScreenAppBar { innerPadding ->
        NavigationSuiteScaffold(
            {
                route.routes.forEach { route -> navigationSuiteRoute(destination, route) }
            },
            modifier,
            layoutType(destination),
            navigationSuiteColors,
            containerColor,
            contentColor,
        ) {
            CompositionLocalProvider(
                LocalDestination provides destination,
                LocalDestinationTitle provides destinationTitle,
                LocalHasPreviousDestination provides hasPreviousDestination,
            ) {
                content(innerPadding)
            }
        }
    }

    // Dynamically set title on navigation.
    val listener = OnDestinationChangedListener { _, destination, _ ->
        destinationTitle = route.single { route -> route.isDestination(destination) }.label
    }

    navController.addOnDestinationChangedListener(listener)

    DisposableEffect(navController) {
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    navigator.HandleAction(navController)

    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}
