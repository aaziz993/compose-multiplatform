import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import clib.presentation.theme.ThemeState
import di.koinConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.koinInject
import org.koin.core.KoinApplication
import presentation.theme.darkColorScheme
import presentation.theme.lightColorScheme
import ui.navigation.presentation.NavScreen

@Composable
public fun App(onNavHostReady: suspend (NavController) -> Unit = {}): Unit =
    KoinApplication(KoinApplication::koinConfiguration) {
        val themeState = koinInject<ThemeState>()

        AppComposable(themeState, onNavHostReady)
    }

@Preview
@Composable
public fun PreviewApp(): Unit = KoinApplicationPreview(KoinApplication::koinConfiguration) {
    AppComposable()
}

@Composable
public fun AppComposable(themeState: ThemeState = ThemeState(), onNavHostReady: suspend (NavController) -> Unit = {}) {
    AppEnvironment(themeState, lightColorScheme, darkColorScheme) {
        NavScreen(onNavHostReady = onNavHostReady)
    }
}

