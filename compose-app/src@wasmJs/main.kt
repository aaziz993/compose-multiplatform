import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_name
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
