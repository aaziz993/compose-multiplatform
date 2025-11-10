package presentation.components.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.components.auth.stateholder.AuthStateHolder
import clib.presentation.components.di.AutoConnectKoinScope
import clib.presentation.components.navigation.stateholder.NavigationAction
import clib.presentation.components.navigation.stateholder.NavigationStateHolder
import clib.presentation.locale.stateholder.LocaleStateHolder
import clib.presentation.theme.DarkColors
import clib.presentation.theme.LightColors
import clib.presentation.theme.stateholder.ThemeStateHolder
import presentation.theme.DarkColorsHighContrast
import presentation.theme.LightColorsHighContrast
import presentation.theme.Shapes
import presentation.theme.Typography
import ui.navigation.presentation.NavScreen

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    themeStateHolder: ThemeStateHolder = koinInject(),
    localeStateHolder: LocaleStateHolder = koinInject(),
    authStateHolder: AuthStateHolder = koinInject(),
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
        MotionScheme.expressive(),
        Shapes,
        Typography,
    ) {
        NavScreen(
            modifier = modifier,
            navigationStateHolder = navigationStateHolder,
        )
    }

    LaunchedEffect(auth.user) {
        navigationStateHolder.action(NavigationAction.NavigateAuth(auth))
    }
}
