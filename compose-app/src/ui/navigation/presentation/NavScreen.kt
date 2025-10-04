@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.components.navigation.AdvancedNavHost
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.components.topappbar.fabNestedScrollConnection
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.components.navigation.Navigator
import klib.data.type.primitives.string.uppercaseFirstChar
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import presentation.components.tooltipbox.AppTooltipBox
import ui.navigation.presentation.viewmodel.NavViewModel

@Suppress("ComposeModifierMissing")
@Composable
public fun NavScreen(
    navViewModel: NavViewModel = koinViewModel<NavViewModel>(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    val startDestination = Home
    var title: String by remember { mutableStateOf(startDestination.label) }
    var isDrawerOpen by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isBackButtonVisible by remember(navBackStackEntry) {
        derivedStateOf { navController.previousBackStackEntry != null }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val fabNestedScrollConnection = scrollBehavior.fabNestedScrollConnection()

    // Dynamically set title on navigation.
    navController.addOnDestinationChangedListener { _, destination, _ ->
        title = destination.route!!.substringAfterLast(".").uppercaseFirstChar()
    }

    AdvancedNavigationSuiteScaffold(
        NavRoute,
        { route ->
            route.item(navController, currentDestination, { label -> label.uppercaseFirstChar() }) { destination ->
                navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination))
            }
        },
        koinInject<Navigator<NavRoute, *>>(),
        Modifier.nestedScroll(fabNestedScrollConnection),
        navController = navController,
        onNavHostReady = onNavHostReady,
        topBar = { adaptiveInfo ->
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    Row {
                        if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED)
                            AppTooltipBox("Menu") {
                                IconButton(
                                    onClick = {
                                        isDrawerOpen = !isDrawerOpen
                                    },
                                ) {
                                    Icon(
                                        imageVector = if (isDrawerOpen) Icons.Filled.Menu else Icons.Outlined.Menu,
                                        contentDescription = "Menu",
                                    )
                                }
                            }


                        if (isBackButtonVisible)
                            AppTooltipBox("Navigate back") {
                                IconButton(
                                    onClick = { navViewModel.action(NavigationAction.NavigateBack) },
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Navigate back",
                                    )
                                }
                            }
                    }
                },
                actions = {
                    AppTooltipBox("Switch theme") {
                        IconButton(
                            onClick = {
                                /* click action */
                            },
                        ) {
                            Icon(
                                imageVector = if (true) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = "Switch theme",
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabNestedScrollConnection.isFabVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                AppTooltipBox("Scroll to top") {
                    ExtendedFloatingActionButton(
                        onClick = {
                            scrollBehavior
                        },
                        shape = CircleShape,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = "Scroll to top",
                        )
                    }
                }
            }
        },
        layoutType = { adaptiveInfo ->
            with(adaptiveInfo) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                    if (isDrawerOpen) NavigationSuiteType.NavigationDrawer else NavigationSuiteType.None
                }
                else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                    currentWindowAdaptiveInfo(),
                )
            }
        },
    ) { innerPadding ->
        AdvancedNavHost(
            navController,
            NavRoute,
            startDestination,
            Modifier.padding(innerPadding),
        ) { route ->
            route.item(
                navigateTo = { _, destination ->
                    navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination))
                },
            ) { navViewModel.action(NavigationAction.NavigateBack) }
        }
    }
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()
