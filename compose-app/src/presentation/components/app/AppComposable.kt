package presentation.components.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import clib.presentation.theme.DarkColors
import clib.presentation.theme.LightColors
import org.koin.compose.viewmodel.koinViewModel
import presentation.locale.viewmodel.LocaleViewModel
import presentation.theme.DarkColorsHighContrast
import presentation.theme.LightColorsHighContrast
import presentation.theme.Shapes
import presentation.theme.Typography
import presentation.theme.viewmodel.ThemeViewModel
import ui.auth.presentation.viewmodel.AuthViewModel
import ui.navigation.presentation.NavScreen

@Composable
public fun AppComposable(
    themeViewModel: ThemeViewModel = koinViewModel(),
    localeViewModel: LocaleViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val theme by themeViewModel.state.collectAsStateWithLifecycle()
    val locale by localeViewModel.state.collectAsStateWithLifecycle()
    val auth by authViewModel.state.collectAsStateWithLifecycle()

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
        NavScreen(onNavHostReady = onNavHostReady)
    }
}
