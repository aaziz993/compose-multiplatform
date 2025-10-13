package presentation.components.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import clib.di.AutoConnectKoinScope
import clib.presentation.AppEnvironment
import clib.presentation.auth.stateholder.AuthStateHolder
import clib.presentation.components.connectivity.ConnectivityGlobalSnackbar
import clib.presentation.components.navigation.Navigator
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
import ui.navigation.presentation.Destination
import ui.navigation.presentation.NavScreen
import ui.navigation.presentation.News

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    themeStateHolder: ThemeStateHolder = koinInject(),
    localeStateHolder: LocaleStateHolder = koinInject(),
    authStateHolder: AuthStateHolder = koinInject(),
    startDestination: Destination = AuthRoute,
    loggedInDestination: Destination = News,
    navigator: Navigator<Destination> = koinInject(),
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {}
): Unit = AutoConnectKoinScope(navController) {
    val theme by themeStateHolder.state.collectAsStateWithLifecycle()
    val locale by localeStateHolder.state.collectAsStateWithLifecycle()
    val auth by authStateHolder.state.collectAsStateWithLifecycle()

    var currentDestination: Destination? by remember { mutableStateOf(null) }

    currentDestination?.let { destination ->
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
                startDestination = destination,
                navigator = navigator,
                navController = navController,
                onNavHostReady = onNavHostReady,
            )
        }
    }

    LaunchedEffect(auth.user) {
        currentDestination = if (auth.user == null) startDestination else loggedInDestination
    }
}
