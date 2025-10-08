@file:OptIn(ExperimentalMaterial3Api::class)

package ui.navigation.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import presentation.components.tooltipbox.AppTooltipBox
import ui.navigation.presentation.viewmodel.NavViewModel
import ui.settings.viewmodel.SettingsAction
import ui.settings.viewmodel.SettingsViewModel

@Suppress("ComposeModifierMissing")
@Composable
public fun NavScreen(
    navViewModel: NavViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    val settingsViewModel: SettingsViewModel = koinViewModel()

    val startDestination = Home
    var isDrawerOpen by remember { mutableStateOf(true) }

    AdvancedNavigationSuiteScaffold(
        route = NavRoute,
        startDestination = startDestination,
        navigationSuiteRoute = { currentDestination, route ->
            route.item(navController, currentDestination, { label -> label.uppercaseFirstChar() }) { destination ->
                navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination))
            }
        },
        navigator = koinInject<Navigator<Destination>>(),
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
                                    ThemeMode.SYSTEM -> settingsViewModel.action(SettingsAction.SetTheme(settingsViewModel.state.value.themeState.theme.copy(mode = ThemeMode.LIGHT)))
                                    ThemeMode.LIGHT -> settingsViewModel.action(SettingsAction.SetTheme(settingsViewModel.state.value.themeState.theme.copy(mode = ThemeMode.DARK)))
                                    ThemeMode.DARK -> settingsViewModel.action(SettingsAction.SetTheme(settingsViewModel.state.value.themeState.theme.copy(mode = ThemeMode.SYSTEM)))
                                }
                            },
                        ) {
                            Icon(
                                imageVector = when (themeMode) {
                                    ThemeMode.SYSTEM -> Icons.Outlined.SettingsBrightness
                                    ThemeMode.LIGHT -> Icons.Outlined.LightMode
                                    ThemeMode.DARK -> Icons.Outlined.DarkMode
                                },
                                contentDescription = "Switch theme",
                            )
                        }
                    }
                },
            )
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
            route.item { action -> navViewModel.action(action) }
        }
    }

    ConnectivityGlobalSnackbar(koinInject())
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()

