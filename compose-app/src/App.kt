import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import di.koinConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.KoinApplicationPreview
import screen.navigation.presentation.NavScreen

@Composable
public fun App(onNavHostReady: suspend (NavController) -> Unit = {}): Unit =
    KoinApplication({ koinConfiguration() }) { NavScreen(onNavHostReady = onNavHostReady) }

@Preview
@Composable
public fun PreviewApp(): Unit =
    KoinApplicationPreview({ koinConfiguration() }) {
        App()
    }
