import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_name
import kotlinx.browser.document
import org.jetbrains.compose.resources.getString
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalBrowserHistoryApi
public suspend fun main() {
    val title = getString(Res.string.app_name)
    onWasmReady {
        document.title = title
        val body = document.body ?: return@onWasmReady
        ComposeViewport(body) {
            App(
                onNavHostReady = { navController -> navController.bindToBrowserNavigation() },
            )
        }
    }
}
