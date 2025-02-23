import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.painterResource
import resources.Res
import resources.flag_bz

public fun main(): Unit = application {
    Window(onCloseRequest = ::exitApplication) {
        Column {
            Image(painterResource(Res.drawable.flag_bz),null)
            Screen()
        }
    }
}
