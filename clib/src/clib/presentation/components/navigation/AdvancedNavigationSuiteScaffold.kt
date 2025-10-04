package clib.presentation.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import clib.data.type.collections.toLaunchedEffect
import clib.presentation.components.dialog.alert.AlertDialog
import clib.presentation.components.navigation.model.NavigationEndpoint
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.event.navigator.Navigator
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import kotlinx.coroutines.launch

@Composable
public fun AdvancedNavigationSuiteScaffold(
    route: NavigationRoute,
    navigationSuiteItem: NavigationSuiteScope.(NavigationEndpoint) -> Unit,
    navigator: Navigator<out NavigationEndpoint>,
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
    topBar: @Composable (WindowAdaptiveInfo) -> Unit = {},
    bottomBar: @Composable (WindowAdaptiveInfo) -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    layoutType: @Composable (WindowAdaptiveInfo) -> NavigationSuiteType = { adaptiveInfo ->
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
    },
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()

    NavigationSuiteScaffold(
        {
            route.navigationChildren.forEach { child -> navigationSuiteItem(child) }
        },
        Modifier.fillMaxSize(),
        layoutType(adaptiveInfo),
        navigationSuiteColors,
        containerColor,
        contentColor,
    ) {
        // Global Snackbar by GlobalSnackbarEventController
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
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

        Scaffold(
            modifier,
            { topBar(adaptiveInfo) },
            { bottomBar(adaptiveInfo) },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton,
            floatingActionButtonPosition,
            containerColor,
            contentColor,
            contentWindowInsets,
        ) { innerPadding ->
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

            navigator.HandleAction(navController)

            content(innerPadding)
        }
    }

    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}
