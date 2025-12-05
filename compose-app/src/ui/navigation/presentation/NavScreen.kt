package ui.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.di.koinInject
import clib.presentation.components.connectivity.Connectivity
import clib.presentation.event.alert.GlobalAlertDialog
import clib.presentation.event.snackbar.GlobalSnackbar
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.navigation.NavigationAction
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.theme.model.Theme
import compose_app.generated.resources.Res
import compose_app.generated.resources.offline
import compose_app.generated.resources.online
import klib.data.auth.model.Auth
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import org.jetbrains.compose.resources.getString
import presentation.components.scaffold.AppBar

@Suppress("UNCHECKED_CAST")
@Composable
public fun NavScreen(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    theme: Theme = Theme(),
    onThemeChange: (Theme) -> Unit = {},
    locales: List<Locale> = emptyList(),
    locale: Locale = Locale.current,
    onLocaleChange: (Locale) -> Unit = {},
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    quickAccess: QuickAccess = QuickAccess(),
    hasBack: Boolean = true,
    hasDrawer: Boolean = true,
    isDrawerOpen: Boolean = true,
    onDrawerToggle: () -> Unit = {},
    layoutType: NavigationSuiteType =
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo(true)),
    navigationSuiteScaffoldState: NavigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(),
    onNavigationAction: (NavigationAction) -> Unit = {},
    navigationSuiteItems: NavigationSuiteScope.() -> Unit = {},
    content: @Composable () -> Unit = {},
): Unit = AppBar(
    modifier = modifier,
    title = title,
    theme = theme,
    onThemeChange = onThemeChange,
    locales = locales,
    locale = locale,
    onLocaleChange = onLocaleChange,
    auth = auth,
    onAuthChange = onAuthChange,
    quickAccess = quickAccess,
    hasBack = hasBack,
    hasDrawer = hasDrawer,
    isDrawerOpen = isDrawerOpen,
    onDrawerToggle = onDrawerToggle,
    onNavigationAction = onNavigationAction,
) { innerPadding ->
    NavigationSuiteScaffold(
        navigationSuiteItems = navigationSuiteItems,
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        layoutType = layoutType,
        state = navigationSuiteScaffoldState,
    ) {
        GlobalSnackbar()

        GlobalAlertDialog()

        Connectivity(
            koinInject(),
            {
                GlobalSnackbarEventController.sendEvent(
                    SnackbarEvent(getString(Res.string.online)),
                )
            },
            {
                GlobalSnackbarEventController.sendEvent(
                    SnackbarEvent(getString(Res.string.offline)),
                )
            },
        )

        content()
    }
}

@Preview
@Composable
public fun PreviewNavScreen(): Unit = NavScreen()
