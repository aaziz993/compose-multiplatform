import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import di.koinConfiguration
import navigation.presentation.NavScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
public fun App(onNavHostReady: suspend (NavController) -> Unit = {}) =
    KoinApplication({ koinConfiguration() }) { NavScreen(onNavHostReady = onNavHostReady) }
