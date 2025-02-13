import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jvm_app.generated.resources.Res
import jvm_app.generated.resources.flag_ao

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        Column {
            Image(org.jetbrains.compose.resources.painterResource(Res.drawable.flag_ao),null)
            Screen()
        }
    }
}
