import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import clib.presentation.AppEnvironment
import clib.presentation.theme.systemColorScheme
import di.koinConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.KoinApplicationPreview
import org.koin.core.KoinApplication
import presentation.navigation.presentation.NavigationScreen

@Composable
public fun App(onNavHostReady: suspend (NavController) -> Unit = {}): Unit =
    KoinApplication(KoinApplication::koinConfiguration) {
        AppScreen(onNavHostReady)
    }

@Preview
@Composable
public fun PreviewApp(): Unit = KoinApplicationPreview(KoinApplication::koinConfiguration) {
    AppScreen()
}

@Composable
private fun AppScreen(onNavHostReady: suspend (NavController) -> Unit = {}): Unit = AppEnvironment {
    MaterialTheme(systemColorScheme()) {
        NavigationScreen(onNavHostReady = onNavHostReady)
    }
}
