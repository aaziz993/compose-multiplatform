import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aaziz993.compose_app.generated.resources.LinuxIcon
import io.github.aaziz993.compose_app.generated.resources.MacOSIcon
import io.github.aaziz993.compose_app.generated.resources.Res
import io.github.aaziz993.compose_app.generated.resources.app_name
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource

public suspend fun main() {
    val title = getString(Res.string.app_name)

    application {
        Window(
                onCloseRequest = ::exitApplication,
                title = title,
                icon =
//                    if (isDevelopmentMode())
                    painterResource(Res.drawable.LinuxIcon)
//                else null,
        ) {
            Column {
                Image(painterResource(Res.drawable.LinuxIcon), null)
                Screen()
            }
        }
    }
}

private fun isDevelopmentMode(): Boolean {
    val classPath = System.getProperty("java.class.path") ?: return true
    return classPath.contains("build") || classPath.contains(".gradle")
}
