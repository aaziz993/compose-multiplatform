import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.aaziz993.compose_app.generated.resources.Res
import io.github.aaziz993.compose_app.generated.resources.app_name
import org.jetbrains.compose.resources.getString

public suspend fun main(): Unit =
    CanvasBasedWindow(getString(Res.string.app_name)) {
        App()
    }
