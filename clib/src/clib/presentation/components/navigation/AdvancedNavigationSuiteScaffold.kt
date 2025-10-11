@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.components.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import clib.data.type.collections.ToLaunchedEffect
import clib.presentation.components.dialog.alert.AlertDialog
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.model.Route
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.noLocalProvidedFor
import klib.data.type.primitives.string.uppercaseFirstChar
import kotlinx.coroutines.launch

public val LocalDestination: ProvidableCompositionLocal<NavDestination?> = staticCompositionLocalOf { noLocalProvidedFor("LocalDestination") }
public val LocalBackButton: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { noLocalProvidedFor("LocalBackButton") }
public val LocalTitle: ProvidableCompositionLocal<String> = staticCompositionLocalOf { noLocalProvidedFor("LocalTitle") }

@Composable
public fun <Dest : Any> AdvancedNavigationSuiteScaffold(
    route: NavigationRoute,
    startDestination: Dest,
    navigator: Navigator<Dest>,
    navigationSuiteRoute: NavigationSuiteScope.(currentDestination: NavDestination?, route: Route) -> Unit,
    modifier: Modifier = Modifier,
    navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    layoutType: @Composable (currentDestination: NavDestination?) -> NavigationSuiteType = {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    },
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var title: String by remember { mutableStateOf("") }
    val isBackButton by remember(navBackStackEntry) {
        derivedStateOf { navController.previousBackStackEntry != null }
    }

    // Dynamically set title on navigation.
    navController.addOnDestinationChangedListener { _, destination, _ ->
        title = destination.route!!.substringAfterLast(".").uppercaseFirstChar()
    }

    NavigationSuiteScaffold(
        {
            route.routes.forEach { route -> navigationSuiteRoute(currentDestination, route) }
        },
        modifier,
        layoutType(currentDestination),
        navigationSuiteColors,
        containerColor,
        contentColor,
    ) {
        val coroutineScope = rememberCoroutineScope()

        // Global Snackbar by GlobalSnackbarEventController
        val snackbarHostState = remember { SnackbarHostState() }
        GlobalSnackbarEventController.events.ToLaunchedEffect(
            snackbarHostState,
        ) { event ->
            coroutineScope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()

                val result = snackbarHostState.showSnackbar(
                    event.message,
                    event.action?.name,
                )

                if (result == SnackbarResult.ActionPerformed) event.action?.action?.invoke()
            }
        }

        // Global AlertDialog by GlobalAlertEventController
        var alertDialogState by remember { mutableStateOf<AlertEvent?>(null) }
        GlobalAlertEventController.events.ToLaunchedEffect { event ->
            alertDialogState = event
        }
        alertDialogState?.let { event ->
            AlertDialog(
                event.message,
                isError = event.isError,
                onConfirm = event.action,
                onCancel = { coroutineScope.launch { GlobalAlertEventController.sendEvent(null) } },
            )
        }

        CompositionLocalProvider(
            LocalDestination provides currentDestination,
            LocalTitle provides title,
            LocalBackButton provides isBackButton,
        ) {
            content()
        }
    }

    navigator.HandleAction(navController)

    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}
