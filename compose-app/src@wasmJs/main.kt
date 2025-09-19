import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import io.github.aaziz993.compose_app.generated.resources.Res
import io.github.aaziz993.compose_app.generated.resources.app_name
import kotlinx.browser.document
import org.jetbrains.compose.resources.getString

@ExperimentalBrowserHistoryApi
public suspend fun main() {
    document.title = getString(Res.string.app_name)
    val body = document.body ?: return
    ComposeViewport(body) {
        App(
            onNavHostReady = { it.bindToBrowserNavigation() },
        )
    }
}
