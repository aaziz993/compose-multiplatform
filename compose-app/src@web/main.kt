import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.bindToBrowserNavigation
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_name
import clib.data.type.favicon
import org.jetbrains.compose.resources.getString
import web.dom.document

@OptIn(ExperimentalBrowserHistoryApi::class)
public suspend fun main() {
    document.favicon(Res.getUri("drawable/compose-multiplatform.svg"), "image/svg+xml")
    document.title = getString(Res.string.app_name)
    ComposeViewport {
        App(
            onNavHostReady = NavController::bindToBrowserNavigation,
        )
    }
}
