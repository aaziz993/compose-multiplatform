import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import di.koinConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.KoinApplicationPreview
import org.koin.core.KoinApplication
import presentation.components.app.AppComposable

@Composable
public fun App(onNavHostReady: suspend (NavController) -> Unit = {}): Unit =
    KoinApplication(KoinApplication::koinConfiguration) {
        AppComposable(onNavHostReady = onNavHostReady)
    }

@Preview
@Composable
public fun PreviewApp(): Unit = KoinApplicationPreview(KoinApplication::koinConfiguration) {
    AppComposable()
}

