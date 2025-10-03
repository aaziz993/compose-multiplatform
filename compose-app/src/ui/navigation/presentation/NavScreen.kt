@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Density
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.event.navigator.Navigator
import clib.presentation.theme.LocalAppDensity
import klib.data.type.primitives.string.uppercaseFirstChar
import kotlinx.serialization.serializer
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Suppress("ComposeModifierMissing")
@Composable
public fun NavScreen(
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    val startDestination: Destination = Destination.Home

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var title: String by remember { mutableStateOf(startDestination.label) }
    var isDrawerOpen by remember { mutableStateOf(true) }
    val isBackButtonVisible by remember { derivedStateOf { navController.previousBackStackEntry != null } }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isScrollUpButtonVisible by rememberSaveable { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection by scrollBehavior.nestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -1) isScrollUpButtonVisible = true
                if (available.y > 1) isScrollUpButtonVisible = false

                return scrollBehavior.nestedScrollConnection.onPreScroll(available, source)
            }
        }
    }

    AdvancedNavigationSuiteScaffold(
        {
            Destination.NavGraph.items(navController, currentDestination) { it }
        },
        koinInject<Navigator<Destination>>(),
        Modifier.nestedScroll(nestedScrollConnection),
        navController = navController,
        onNavHostReady = onNavHostReady,
        topBar = { adaptiveInfo ->
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    Row {
                        if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                            TooltipBox(
                                positionProvider =
                                    TooltipDefaults.rememberTooltipPositionProvider(
                                        TooltipAnchorPosition.Above,
                                    ),
                                tooltip = { PlainTooltip { Text("Menu") } },
                                state = rememberTooltipState(),
                            ) {
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
                        }

                        if (isBackButtonVisible)
                            TooltipBox(
                                positionProvider =
                                    TooltipDefaults.rememberTooltipPositionProvider(
                                        TooltipAnchorPosition.Above,
                                    ),
                                tooltip = { PlainTooltip { Text("NavigateBack") } },
                                state = rememberTooltipState(),
                            ) {
                                IconButton(
                                    onClick = {

                                    },
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
                    IconButton(
                        onClick = {
                            /* click action */
                        },
                    ) {
                        Icon(
                            imageVector = if (true) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Share items",
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isScrollUpButtonVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        scrollBehavior
                    },
                    shape = CircleShape,
                ) {
                    Text(
                        text = "Extended FAB",
                    )
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
        NavScreenNavHost(
            navController,
            startDestination,
            Modifier.padding(innerPadding),
            route = Destination.NavGraph::class,
        )
    }

    navController.addOnDestinationChangedListener { controller, destination, args ->
        title = destination.route!!.substringAfterLast(".").uppercaseFirstChar()
    }
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()
