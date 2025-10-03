@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.ui.presentation.event.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Suppress("ComposeModifierMissing")
@Composable
public fun NavigationScreen(
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    NavigationScaffold(
        { currentDestination ->
            navScreenNavigationSuiteItems(
                navController,
                currentDestination,
            ) { this::class.simpleName!! }
        },
        koinInject<Navigator<Destination>>(),
        navController = navController,
        onNavHostReady = onNavHostReady,
        topBar = {
            TopAppBar(
                title = { Text("Compose App") },
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
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED)
                    NavigationSuiteType.NavigationDrawer
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
