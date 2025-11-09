@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.window.core.layout.WindowSizeClass
import clib.data.type.collections.ToLaunchedEffect
import clib.data.type.primitives.string.toStringResource
import clib.di.koinInject
import clib.presentation.components.auth.LocalAuth
import clib.presentation.components.connectivity.Connectivity
import clib.presentation.components.dialog.alert.AlertDialog
import clib.presentation.components.navigation.NavDisplay
import clib.presentation.components.navigation.hasNavigationItems
import clib.presentation.components.navigation.items
import clib.presentation.components.navigation.stateholder.NavigationAction
import clib.presentation.components.navigation.stateholder.NavigationStateHolder
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import kotlinx.coroutines.launch

@Composable
public fun NavScreen(
    modifier: Modifier = Modifier,
    navigationStateHolder: NavigationStateHolder = koinInject(),
) {
    val auth = LocalAuth.current
    val currentRoute = navigationStateHolder.backStack.last()
    val drawerStateHolder: DrawerStateHolder = koinInject()

    currentRoute.navRoute.ParentContent {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                Routes.items(
                    auth = auth,
                    currentRoute = currentRoute,
                    transform = { name -> name.toStringResource(Res.allStringResources) },
                ) { route -> navigationStateHolder.action(NavigationAction.Navigate(route)) }
            },
            modifier = modifier,
            layoutType = if (Routes.hasNavigationItems(auth))
                with(currentWindowAdaptiveInfo()) {
                    if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) {
                        if (drawerStateHolder.isOpen) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
                    }
                    else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(this)
                }
            else NavigationSuiteType.None,
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

            Connectivity(
                connectivity = koinInject(),
                connected = {
                    GlobalSnackbarEventController.sendEvent(
                        SnackbarEvent("Online"),
                    )
                },
                disconnected = {
                    GlobalSnackbarEventController.sendEvent(
                        SnackbarEvent("Offline"),
                    )
                },
            )

            NavDisplay(
                backStack = navigationStateHolder.backStack,
                onBack = { navigationStateHolder.action(NavigationAction.NavigateBack) },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                transitionSpec = {
                    // Slide in from right when navigating forward
                    slideInHorizontally(initialOffsetX = { it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { -it })
                },
                popTransitionSpec = {
                    // Slide in from left when navigating back
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
                },
                predictivePopTransitionSpec = {
                    // Slide in from left for predictive back gestures
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
                },
            )
        }
    }
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()

