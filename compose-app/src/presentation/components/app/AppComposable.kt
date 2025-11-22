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
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.systemOnBack
import clib.presentation.theme.ThemeState
import clib.presentation.theme.rememberThemeState
import kotlinx.coroutines.launch
import presentation.theme.DarkColors
import presentation.theme.DarkColorsHighContrast
import presentation.theme.LightColors
import presentation.theme.LightColorsHighContrast
import presentation.theme.Shapes
import presentation.theme.Typography
import ui.navigation.presentation.App
import ui.navigation.presentation.Articles
import ui.navigation.presentation.Auth
import ui.navigation.presentation.Services

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    themeState: ThemeState = rememberThemeState(),
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = koinInject(),
    routes: Routes = App,
    router: Router = koinInject(),
    navigatorFactory: @Composable (Routes) -> Navigator = {
        val coroutineScope = rememberCoroutineScope()
        rememberNav3Navigator(
            it,
            authState.auth,
            Auth,
            if (it == routes) Services else null,
            systemOnBack(),
        ) { exception ->
            coroutineScope.launch {
                GlobalAlertEventController.sendEvent(
                    AlertEvent(exception.message.orEmpty(), true),
                )
            }
        }
    },
): Unit = AutoConnectKoinScope(router) {
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
        routes,
        router,
        navigatorFactory,
    )
}

@Preview
@Composable
public fun PreviewAppComposable(): Unit = AppComposable()
