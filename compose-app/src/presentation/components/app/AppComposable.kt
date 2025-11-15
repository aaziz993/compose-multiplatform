package presentation.components.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.auth.AuthState
import clib.presentation.components.di.AutoConnectKoinScope
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.NavigationException
import clib.presentation.navigation.Router
import clib.presentation.navigation.platformOnBack
import clib.presentation.navigation.rememberNavBackStack
import clib.presentation.theme.DarkColors
import clib.presentation.theme.LightColors
import clib.presentation.theme.ThemeState
import clib.presentation.theme.rememberThemeState
import presentation.theme.DarkColorsHighContrast
import presentation.theme.LightColorsHighContrast
import presentation.theme.Shapes
import presentation.theme.Typography
import ui.navigation.presentation.App
import ui.navigation.presentation.Auth
import ui.navigation.presentation.NavScreen
import ui.navigation.presentation.News

@Suppress("UNCHECKED_CAST")
@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    themeState: ThemeState = rememberThemeState(),
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = koinInject(),
    protectedRoute: NavRoute = News,
    authRoute: NavRoute = Auth,
    publicRoute: NavRoute = authRoute,
    backStack: NavBackStack<NavRoute> = App.rememberNavBackStack(),
    router: Router = koinInject(),
    onNavigateBack: () -> Unit = platformOnBack(),
    onNavigationError: suspend (NavigationException) -> Unit = { exception ->
        GlobalAlertEventController.sendEvent(AlertEvent(exception.message.orEmpty(), true))
    },
): Unit = AutoConnectKoinScope(backStack = backStack) {
    AppEnvironment(
        themeState,
        LightColors,
        LightColorsHighContrast,
        DarkColors,
        DarkColorsHighContrast,
        localeState,
        authState,
        protectedRoute,
        authRoute,
        publicRoute,
        backStack,
        onNavigateBack,
        onNavigationError,
        router,
        MotionScheme.expressive(),
        Shapes,
        Typography,
    ) {
        NavScreen(
            modifier,
            themeState,
            localeState,
            authState,
            authRoute,
            publicRoute,
            protectedRoute,
            backStack,
            router,
        )
    }
}
