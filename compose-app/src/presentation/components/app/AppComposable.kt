package presentation.components.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.auth.AuthState
import clib.presentation.components.di.AutoConnectKoinScope
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.exception.NavigationException
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
import ui.navigation.presentation.Protected
import ui.navigation.presentation.Public

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    themeState: ThemeState = rememberThemeState(),
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = koinInject(),
    router: Router = koinInject(),
    routes: Routes = App,
    publicRoute: NavRoute = Public,
    protectedRoute: NavRoute? = Protected,
    authRoute: NavRoute? = Auth,
    onError: suspend (NavigationException) -> Unit = { exception ->
        GlobalAlertEventController.sendEvent(AlertEvent(exception.message.orEmpty(), true))
    },
): Unit = AutoConnectKoinScope(router) {
    AppEnvironment(
        themeState,
        LightColors,
        LightColorsHighContrast,
        DarkColors,
        DarkColorsHighContrast,
        localeState,
        authState,
        MotionScheme.expressive(),
        Shapes,
        Typography,
    ) {
        App.Content(
            router = router,
            startRoute = if (authState.auth.user == null) publicRoute else requireNotNull(protectedRoute) { "No protected route" },
            authRoute = authRoute,
            auth = authState.auth,
            onError = onError,
        )
    }
}

@Preview
@Composable
public fun PreviewAppComposable(): Unit = AppComposable()
