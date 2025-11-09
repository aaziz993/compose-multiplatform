package presentation.components.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import clib.presentation.AppEnvironment
import clib.presentation.components.auth.stateholder.AuthStateHolder
import clib.presentation.components.navigation.stateholder.NavigationStateHolder
import clib.presentation.components.navigation.Route
import clib.presentation.locale.stateholder.LocaleStateHolder
import clib.presentation.components.navigation.stateholder.NavigationAction
import clib.presentation.theme.DarkColors
import clib.presentation.theme.LightColors
import clib.presentation.theme.stateholder.ThemeStateHolder
import clib.di.koinInject
import clib.presentation.components.di.AutoConnectKoinScope
import presentation.theme.DarkColorsHighContrast
import presentation.theme.LightColorsHighContrast
import presentation.theme.Shapes
import presentation.theme.Typography
import ui.navigation.presentation.Articles
import ui.navigation.presentation.NavScreen
import ui.navigation.presentation.Phone

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    themeStateHolder: ThemeStateHolder = koinInject(),
    localeStateHolder: LocaleStateHolder = koinInject(),
    authStateHolder: AuthStateHolder = koinInject(),
    loginStartRoute: Route = Phone,
    loggedInStartRoute: Route = Articles,
    navigationStateHolder: NavigationStateHolder = koinInject(),
): Unit = AutoConnectKoinScope(backStack = navigationStateHolder.backStack) {
    val theme by themeStateHolder.state.collectAsStateWithLifecycle()
    val locale by localeStateHolder.state.collectAsStateWithLifecycle()
    val auth by authStateHolder.state.collectAsStateWithLifecycle()

    AppEnvironment(
        theme,
        locale,
        auth,
        LightColors,
        LightColorsHighContrast,
        DarkColors,
        DarkColorsHighContrast,
        Shapes,
        Typography,
    ) {
        NavScreen(
            modifier = modifier,
            navigationStateHolder = navigationStateHolder,
        )
    }

    LaunchedEffect(auth.user) {
        navigationStateHolder.action(
            NavigationAction.NavigateAndClear(
                if (auth.user == null) loginStartRoute else loggedInStartRoute,
            ),
        )
    }
}
