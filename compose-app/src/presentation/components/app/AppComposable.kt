package presentation.components.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import org.koin.compose.viewmodel.koinViewModel
import presentation.components.app.viewmodel.AppViewModel
import presentation.theme.darkColorScheme
import presentation.theme.darkColorSchemeHighContrast
import presentation.theme.lightColorScheme
import presentation.theme.lightColorSchemeHighContrast
import ui.navigation.presentation.NavScreen

@Composable
public fun AppComposable(
    viewModel: AppViewModel = koinViewModel(),
    onNavHostReady: suspend (NavController) -> Unit = {}
): Unit = AppEnvironment(
    viewModel.themeState,
    lightColorScheme,
    lightColorSchemeHighContrast,
    darkColorScheme,
    darkColorSchemeHighContrast,
) {
    NavScreen(onNavHostReady = onNavHostReady)
}
