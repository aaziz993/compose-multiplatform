import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aaziz993.cmp_lib.generated.resources.app_name
import io.github.aaziz993.compose_app.generated.resources.LinuxIcon
import io.github.aaziz993.compose_app.generated.resources.Res
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource

public suspend fun main() {
    val applicationName = getString(Res.string.app_name)

    application {

        Window(onCloseRequest = ::exitApplication) {
            Column {
                Image(painterResource(Res.drawable.LinuxIcon),null)
                Screen()
            }
        }
    }
}
