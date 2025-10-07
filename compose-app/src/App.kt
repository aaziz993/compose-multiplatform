import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import clib.presentation.theme.ThemeState
import di.koinConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.KoinApplicationPreview
import org.koin.core.KoinApplication
import presentation.theme.darkColorScheme
import presentation.theme.darkColorSchemeHighContrast
import presentation.theme.lightColorScheme
import presentation.theme.lightColorSchemeHighContrast
import ui.navigation.presentation.NavScreen

@Composable
public fun App(onNavHostReady: suspend (NavController) -> Unit = {}): Unit =
    KoinApplication(KoinApplication::koinConfiguration) {
        AppComposable(onNavHostReady)
    }

@Preview
@Composable
public fun PreviewApp(): Unit = KoinApplicationPreview(KoinApplication::koinConfiguration) {
    AppComposable()
}

@Composable
public fun AppComposable(onNavHostReady: suspend (NavController) -> Unit = {}) {
    val themeState = ThemeState()

    AppEnvironment(
        themeState,
        lightColorScheme,
        lightColorSchemeHighContrast,
        darkColorScheme,
        darkColorSchemeHighContrast,
    ) {
        NavScreen(onNavHostReady = onNavHostReady)
    }
}

