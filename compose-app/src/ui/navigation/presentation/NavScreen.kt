@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.data.type.collections.ToLaunchedEffect
import clib.presentation.auth.LocalAuth
import clib.presentation.components.connectivity.ConnectivityGlobalSnackbar
import clib.presentation.components.dialog.alert.AlertDialog
import clib.presentation.components.navigation.AdvancedNavHost
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.components.navigation.Navigator
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.stateholders.BooleanStateHolder
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import klib.data.type.primitives.string.uppercaseFirstChar
import kotlinx.coroutines.launch
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
        layoutType = {
            val hasNavigationItems = NavRoute.filterNot(AuthRoute::contains).toList().any { route ->
                route.canNavigateItem(LocalAuth.current)
            }
            if (hasNavigationItems)
                with(currentWindowAdaptiveInfo()) {
                    if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                        if (isDrawerOpen) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
                    }
                    else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
                }
            else NavigationSuiteType.None
        },
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

        AdvancedNavHost(
            navController = navController,
            route = NavRoute,
            startDestination = startDestination,
        ) { route ->
            route.item { action -> navigator.navigate(action) }
        }

        ConnectivityGlobalSnackbar(koinInject())
    }
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()

