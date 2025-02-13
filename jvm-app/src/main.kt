import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import `jvm-app`.resources.Res
import `jvm-app`.resources.flag_bz
import org.jetbrains.compose.resources.painterResource

public fun main(): Unit = application {
    Window(onCloseRequest = ::exitApplication) {
        Column {
            Image(painterResource(Res.drawable.flag_bz),null)
            Screen()
        }
    }
}
