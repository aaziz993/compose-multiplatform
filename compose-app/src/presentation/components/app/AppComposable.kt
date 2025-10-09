package presentation.components.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import clib.presentation.theme.viewmodel.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.theme.Shapes
import presentation.theme.Typography
import presentation.theme.darkColorScheme
import presentation.theme.darkColorSchemeHighContrast
import presentation.theme.lightColorScheme
import presentation.theme.lightColorSchemeHighContrast
import ui.navigation.presentation.NavScreen

@Composable
public fun AppComposable(
    themeViewModel: ThemeViewModel = koinViewModel(),
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val theme by themeViewModel.state.collectAsStateWithLifecycle()

    AppEnvironment(
        theme,
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
