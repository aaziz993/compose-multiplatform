import androidx.compose.ui.ExperimentalComposeUiApi
import io.github.aaziz993.compose_app.generated.resources.Res
import io.github.aaziz993.compose_app.generated.resources.app_name
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalComposeUiApi::class)
public suspend fun main(): Unit =
    CanvasBasedWindow(getString(Res.string.app_name)) {
        App()
    }
