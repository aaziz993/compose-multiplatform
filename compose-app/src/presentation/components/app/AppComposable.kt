package presentation.components.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import org.koin.compose.viewmodel.koinViewModel
import presentation.locale.viewmodel.LocaleViewModel
import presentation.theme.Shapes
import presentation.theme.Typography
import presentation.theme.darkColorScheme
import presentation.theme.darkColorSchemeHighContrast
import presentation.theme.lightColorScheme
import presentation.theme.lightColorSchemeHighContrast
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
        lightColorScheme,
        lightColorSchemeHighContrast,
        darkColorScheme,
        darkColorSchemeHighContrast,
        Shapes,
        Typography,
    ) {
        NavScreen(onNavHostReady = onNavHostReady)
    }
}
