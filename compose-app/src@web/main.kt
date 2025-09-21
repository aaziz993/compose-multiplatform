import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.bindToBrowserNavigation
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_name
import org.jetbrains.compose.resources.getString
import web.dom.document

@OptIn(ExperimentalComposeUiApi::class)
public suspend fun main() {
    document.title = getString(Res.string.app_name)
    ComposeViewport {
        App(
            onNavHostReady = { it.bindToBrowserNavigation() },
        )
    }
}
