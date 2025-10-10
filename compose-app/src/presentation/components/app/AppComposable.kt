package presentation.components.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import clib.presentation.auth.LocalAppAuth
import clib.presentation.auth.stateholder.AuthStateHolder
import clib.presentation.components.navigation.model.Route
import clib.presentation.locale.stateholder.LocaleStateHolder
import clib.presentation.theme.DarkColors
import clib.presentation.theme.LightColors
import clib.presentation.theme.stateholder.ThemeStateHolder
import org.koin.compose.koinInject
import presentation.theme.DarkColorsHighContrast
import presentation.theme.LightColorsHighContrast
import presentation.theme.Shapes
import presentation.theme.Typography
import ui.navigation.presentation.AuthRoute
import ui.navigation.presentation.NavScreen
import ui.navigation.presentation.News

@Composable
public fun AppComposable(
    themeStateHolder: ThemeStateHolder = koinInject(),
    localeStateHolder: LocaleStateHolder = koinInject(),
    authStateHolder: AuthStateHolder = koinInject(),
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val theme by themeStateHolder.state.collectAsStateWithLifecycle()
    val locale by localeStateHolder.state.collectAsStateWithLifecycle()
    val auth by authStateHolder.state.collectAsStateWithLifecycle()

    val startDestination: Route = remember(auth.user) { if (auth.user == null) AuthRoute else News }

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
            startDestination = startDestination,
            onNavHostReady = onNavHostReady,
        )
    }
}
