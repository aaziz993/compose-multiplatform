package presentation.components.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import clib.presentation.auth.stateholder.AuthStateHolder
import clib.presentation.auth.LocalAppAuth
import clib.presentation.locale.stateholder.LocaleStateHolder
import clib.presentation.theme.DarkColors
import clib.presentation.theme.LightColors
import clib.presentation.theme.stateholder.ThemeStateHolder
import org.koin.compose.koinInject
import presentation.theme.DarkColorsHighContrast
import presentation.theme.LightColorsHighContrast
import presentation.theme.Shapes
import presentation.theme.Typography
import ui.navigation.presentation.NavScreen
import ui.navigation.presentation.News
import ui.navigation.presentation.PhoneCheckUp

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
            startDestination = if (LocalAppAuth.current.user == null) PhoneCheckUp else News,
            onNavHostReady = onNavHostReady,
        )
    }
}
