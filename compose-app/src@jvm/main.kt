import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_name
import compose_app.generated.resources.compose_multiplatform_ico
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource

private val INIT_SIZE = DpSize(800.dp, 600.dp)

public suspend fun main() {
    val title = getString(Res.string.app_name)

    application {
        val windowState = rememberWindowState(width = INIT_SIZE.width, height = INIT_SIZE.height)

        Window(
            onCloseRequest = ::exitApplication,
            windowState,
            title = title,
            icon = if (isDevelopmentMode()) painterResource(Res.drawable.compose_multiplatform_ico)
            else null,
        ) {
            App()
        }
    }
}

private fun isDevelopmentMode(): Boolean {
    val classPath = System.getProperty("java.class.path") ?: return true
    return classPath.contains("build") || classPath.contains(".gradle")
}
