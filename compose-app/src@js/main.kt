import Screen
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.aaziz993.compose_app.generated.resources.Res
import io.github.aaziz993.compose_app.generated.resources.app_name
import kotlinx.browser.document
import org.jetbrains.compose.resources.getString
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
public suspend fun main() {
    val title = getString(Res.string.app_name)

    onWasmReady {
        CanvasBasedWindow(title) {
            Screen()
        }
    }
}
