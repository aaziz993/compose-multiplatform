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
import clib.data.type.collections.toLaunchedEffect
import clib.presentation.components.dialog.alert.AlertDialog
import clib.presentation.components.navigation.model.NavigationDestination
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.model.Route
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.noLocalProvidedFor
import klib.data.type.primitives.string.uppercaseFirstChar
import kotlinx.coroutines.launch

public val LocalAppTitle: ProvidableCompositionLocal<String> = staticCompositionLocalOf { noLocalProvidedFor("LocalTitle") }
public val LocalAppBackButton: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { noLocalProvidedFor("LocalBackButton") }

@Composable
public fun <Dest : Any> AdvancedNavigationSuiteScaffold(
    route: NavigationRoute,
    startDestination: NavigationDestination<Dest>,
    navigationSuiteRoute: NavigationSuiteScope.(currentDestination: NavDestination?, route: Route) -> Unit,
    navigator: Navigator<*>,
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
    navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    layoutType: NavigationSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
    content: @Composable () -> Unit
) {
    var title: String by remember { mutableStateOf(startDestination.label) }
    // Dynamically set title on navigation.
    navController.addOnDestinationChangedListener { _, destination, _ ->
        title = destination.route!!.substringAfterLast(".").uppercaseFirstChar()
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isBackButton by remember(navBackStackEntry) {
        derivedStateOf { navController.previousBackStackEntry != null }
    }

    NavigationSuiteScaffold(
        {
            route.navigationChildren.forEach { child -> navigationSuiteRoute(currentDestination, child) }
        },
        modifier,
        layoutType,
        navigationSuiteColors,
        containerColor,
        contentColor,
    ) {
        val scope = rememberCoroutineScope()

        // Global Snackbar by GlobalSnackbarEventController
        val snackbarHostState = remember { SnackbarHostState() }
        GlobalSnackbarEventController.events.toLaunchedEffect(
            snackbarHostState,
        ) { event ->
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()

                val result = snackbarHostState.showSnackbar(
                    event.message,
                    event.action?.name,
                )

                if (result == SnackbarResult.ActionPerformed) {
                    event.action?.action?.invoke()
                }
            }
        }

        // Global AlertDialog by GlobalAlertEventController
        var alertDialogState by remember { mutableStateOf<AlertEvent?>(null) }
        GlobalAlertEventController.events.toLaunchedEffect { event ->
            alertDialogState = event
        }
        alertDialogState?.let {
            AlertDialog(
                it.message,
                isError = it.isError,
                onConfirm = it.action,
                onCancel = { scope.launch { GlobalAlertEventController.sendEvent(null) } },
            )
        }

        CompositionLocalProvider(
            LocalAppTitle provides title,
            LocalAppBackButton provides isBackButton,
        ) {
            content()
        }
    }

    navigator.HandleAction(navController)

    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}
