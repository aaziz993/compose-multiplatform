package presentation.components.app

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import clib.di.koinInject
import clib.presentation.AppEnvironment
import clib.presentation.auth.AuthState
import clib.presentation.density.DensityState
import clib.presentation.density.rememberDensityState
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.locale.LocaleState
import clib.presentation.locale.rememberLocaleState
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import clib.presentation.navigation.rememberNav3Navigator
import clib.presentation.navigation.rememberRouter
import clib.presentation.navigation.systemOnBack
import clib.presentation.theme.ThemeState
import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.toColorScheme
import clib.presentation.theme.rememberThemeState
import kotlinx.coroutines.launch
import presentation.theme.DarkColors
import presentation.theme.LightColors
import presentation.theme.SquircleShapes
import ui.navigation.presentation.App
import ui.navigation.presentation.Auth
import ui.navigation.presentation.Services
import  androidx.compose.material3.Typography

@Composable
public fun AppComposable(
    modifier: Modifier = Modifier.fillMaxSize(),
    themeState: ThemeState = rememberThemeState(
        Theme(
            lightColorScheme = LightColors.toColorScheme(),
            darkColorScheme = DarkColors.toColorScheme(),
        ),
    ),
    densityState: DensityState = rememberDensityState(),
    localeState: LocaleState = rememberLocaleState(),
    authState: AuthState = koinInject(),
    motionScheme: MotionScheme? = MotionScheme.expressive(),
    shapes: Shapes? = SquircleShapes,
    typography: Typography? = presentation.theme.Typography,
    animationSpec: FiniteAnimationSpec<Color> = spring(),
    routes: Routes = App,
    routerFactory: @Composable (Routes) -> Router = { routes -> rememberRouter(routes) },
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
): Unit = AppEnvironment(
    themeState,
    densityState,
    localeState,
    authState,
    motionScheme,
    shapes,
    typography,
    animationSpec,
    routerFactory,
    navigatorFactory,
    routes,
)

@Preview
@Composable
public fun PreviewAppComposable(): Unit = AppComposable()
