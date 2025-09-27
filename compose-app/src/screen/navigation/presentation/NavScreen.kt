package screen.navigation.presentation

import ai.tech.core.misc.type.multiple.toLaunchedEffect
import ai.tech.core.presentation.component.dialog.alert.AlertDialog
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import presentation.event.alert.GlobalAlertEventController
import presentation.event.alert.model.AlertEvent
import presentation.event.navigator.Navigator
import presentation.event.snackbar.GlobalSnackbarEventController

@Composable
public fun NavScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    MaterialTheme {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val adaptiveInfo = currentWindowAdaptiveInfo()

        val customNavSuiteType = with(adaptiveInfo) {
            if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                NavigationSuiteType.NavigationDrawer
            }
            else {
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                    currentWindowAdaptiveInfo(),
                )
            }
        }

        NavigationSuiteScaffold(
            navScreenNavigationSuiteItems(
                navController,
                currentDestination,
                "home_main",
                "home_map",
                "home_settings",
                "home_about",
                "auth_login",
                "auth_profile",
                "wallet_balance",
                "wallet_crypto",
                "wallet_stock",
            ),
            modifier.fillMaxSize(),
            customNavSuiteType,
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
                modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(snackbarHostState) },
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

                val navigator = koinInject<Navigator<Destination>>()

                navigator.handleAction(navController)

                NavScreenNavHost(
                    navController,
                    Destination.Main,
                    modifier.padding(innerPadding),
                    route = Destination.NavGraph::class,
                )
            }
        }
    }

    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}

@Preview
@Composable
public fun previewNavScreen() {
    NavScreen()
}
