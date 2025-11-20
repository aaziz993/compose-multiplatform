package presentation.components.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.auth.AuthState
import clib.presentation.components.di.AutoConnectKoinScope
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.exception.NavigationException
import clib.presentation.theme.ThemeState
import clib.presentation.theme.rememberThemeState
import presentation.theme.DarkColors
import presentation.theme.DarkColorsHighContrast
import presentation.theme.LightColors
import presentation.theme.LightColorsHighContrast
import presentation.theme.Shapes
import presentation.theme.Typography
import ui.navigation.presentation.App
import ui.navigation.presentation.Auth
import ui.navigation.presentation.News

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    themeState: ThemeState = rememberThemeState(),
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = koinInject(),
    router: Router = koinInject(),
    routes: Routes = App,
    authRoute: NavRoute? = Auth,
    authRedirectRoute: NavRoute? = News,
    onBack: (() -> Unit)? = null,
    onError: ((NavigationException) -> Unit)? = null,
): Unit = AutoConnectKoinScope(router) {
    val coroutineScope = rememberCoroutineScope()
    AppEnvironment(
        themeState,
        LightColors,
        LightColorsHighContrast,
        DarkColors,
        DarkColorsHighContrast,
        MotionScheme.expressive(),
        Shapes,
        Typography,
        localeState,
        authState,
        router,
        routes,
        authRoute,
        authRedirectRoute,
        onBack,
        onError,
    )
}

@Preview
@Composable
public fun PreviewAppComposable(): Unit = AppComposable()
