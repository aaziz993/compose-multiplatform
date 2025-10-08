package presentation.components.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import org.koin.compose.viewmodel.koinViewModel
import presentation.theme.darkColorScheme
import presentation.theme.darkColorSchemeHighContrast
import presentation.theme.lightColorScheme
import presentation.theme.lightColorSchemeHighContrast
import ui.navigation.presentation.NavScreen
import ui.settings.viewmodel.SettingsViewModel

@Composable
public fun AppComposable(
    settingsViewModel: SettingsViewModel = koinViewModel(),
    onNavHostReady: suspend (NavController) -> Unit = {}
): Unit = AppEnvironment(
    settingsViewModel.state.value.themeState,
    lightColorScheme,
    lightColorSchemeHighContrast,
    darkColorScheme,
    darkColorSchemeHighContrast,
) {
    NavScreen(onNavHostReady = onNavHostReady)
}
