@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Unarchive
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
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.components.connectivity.ConnectivityGlobalSnackbar
import clib.presentation.components.navigation.AdvancedNavHost
import clib.presentation.components.navigation.AdvancedNavigationSuiteScaffold
import clib.presentation.components.navigation.Navigator
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.model.ThemeMode
import klib.data.type.primitives.string.uppercaseFirstChar
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import presentation.components.app.viewmodel.AppAction
import presentation.components.app.viewmodel.AppViewModel
import presentation.components.tooltipbox.AppTooltipBox
import ui.navigation.presentation.viewmodel.NavViewModel

@Suppress("ComposeModifierMissing")
@Composable
public fun NavScreen(
    navViewModel: NavViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    val appViewModel: AppViewModel = koinViewModel()

    val startDestination = Home
    var isDrawerOpen by remember { mutableStateOf(true) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val isFabButton by remember {
        derivedStateOf { scrollBehavior.state.heightOffset < 0f }
    }

    navController.addOnDestinationChangedListener { _, _, _ ->
        scrollBehavior.state.heightOffset = 0f
    }

    AdvancedNavigationSuiteScaffold(
        NavRoute,
        startDestination,
        { currentDestination, route ->
            route.item(navController, currentDestination, { label -> label.uppercaseFirstChar() }) { destination ->
                navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination))
            }
        },
        koinInject<Navigator<NavRoute, *>>(),
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        navController = navController,
        onNavHostReady = onNavHostReady,
        topBar = { adaptiveInfo, title, isBackButton ->
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


                        if (isBackButton)
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
                    val themeMode = LocalAppTheme.current.mode
                    AppTooltipBox("Switch theme") {
                        IconButton(
                            onClick = {
                                when (themeMode) {
                                    ThemeMode.SYSTEM -> appViewModel.action(AppAction.SetTheme(appViewModel.themeState.theme.copy(mode = ThemeMode.LIGHT)))
                                    ThemeMode.LIGHT -> appViewModel.action(AppAction.SetTheme(appViewModel.themeState.theme.copy(mode = ThemeMode.DARK)))
                                    ThemeMode.DARK -> appViewModel.action(AppAction.SetTheme(appViewModel.themeState.theme.copy(mode = ThemeMode.SYSTEM)))
                                }
                            },
                        ) {
                            Icon(
                                imageVector = when (themeMode) {
                                    ThemeMode.SYSTEM -> Icons.Filled.LightMode
                                    ThemeMode.LIGHT -> Icons.Filled.DarkMode
                                    ThemeMode.DARK -> Icons.Filled.Unarchive
                                },
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
                visible = isFabButton,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                AppTooltipBox("Scroll to top") {
                    ExtendedFloatingActionButton(
                        onClick = {
                            scrollBehavior.state.heightOffset = 0f
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
    ) {
        AdvancedNavHost(
            navController,
            NavRoute,
            startDestination,
        ) { route ->
            route.item(
                navigateTo = { _, destination ->
                    navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination))
                },
            ) { navViewModel.action(NavigationAction.NavigateBack) }
        }
    }

    ConnectivityGlobalSnackbar(koinInject())
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()

