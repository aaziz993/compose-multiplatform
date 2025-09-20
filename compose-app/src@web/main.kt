import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.bindToBrowserNavigation
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_name
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
//    document.title = getString(Res.string.app_name)
//    val body = document.body ?: return
    ComposeViewport {
        App(
            onNavHostReady = { it.bindToBrowserNavigation() },
        )
    }
}
