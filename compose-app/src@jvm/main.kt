import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import clib.presentation.events.deeplink.GlobalDeeplinkEventController
import clib.presentation.events.deeplink.handleEvents
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_icon
import compose_app.generated.resources.app_name
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource

private val INIT_SIZE = DpSize(800.dp, 600.dp)

public suspend fun main(args: Array<String>) {
    // Prevent SwingPanel on top of compose components.
    System.setProperty("compose.interop.blending", "true")
    System.setProperty("compose.swing.render.on.graphics", "true")

    // Handle deep link events.
    GlobalDeeplinkEventController.handleEvents(args)

    val title = getString(Res.string.app_name)
    application {
        val windowState = rememberWindowState(width = INIT_SIZE.width, height = INIT_SIZE.height)

        Window(
            onCloseRequest = ::exitApplication,
            windowState,
            title = title,
            icon = if (isDevelopmentMode()) painterResource(Res.drawable.app_icon)
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
