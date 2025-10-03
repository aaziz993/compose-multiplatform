@file:OptIn(ExperimentalMaterial3Api::class)

package navigation.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.event.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Suppress("ComposeModifierMissing")
@Composable
public fun NavigationScreen(
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    var drawerStateOpen by remember { mutableStateOf(true) }

    AdvancedNavigationSuiteScaffold(
        { currentDestination ->
            navScreenNavigationSuiteItems(
                navController,
                currentDestination,
            ) { this::class.simpleName!! }
        },
        koinInject<Navigator<Destination>>(),
        navController = navController,
        onNavHostReady = onNavHostReady,
        topBar = { adaptiveInfo ->
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                        IconButton(
                            onClick = {
                                drawerStateOpen = !drawerStateOpen
                            },
                        ) {
                            Icon(
                                imageVector = if (drawerStateOpen) Icons.Filled.ArrowCircleLeft else Icons.Filled.ArrowCircleRight,
                                contentDescription = "Share items",
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            /* click action */
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share items",
                        )
                    }
                },
            )
        },
        layoutType = { adaptiveInfo ->
            with(adaptiveInfo) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                    if (drawerStateOpen) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
                }
                else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                    currentWindowAdaptiveInfo(),
                )
            }
        },
    ) { innerPadding ->
        NavScreenNavHost(
            navController,
            Destination.Main,
            Modifier.padding(innerPadding),
            route = Destination.NavGraph::class,
        )
    }
}

@Preview
@Composable
public fun PreviewNavigationScreen(): Unit = NavigationScreen()
