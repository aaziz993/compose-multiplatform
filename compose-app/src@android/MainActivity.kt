package hello.world

import Screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import io.github.aaziz993.compose_app.generated.resources.Res
import io.github.aaziz993.compose_app.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

public class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Screen()
                Image(painterResource(Res.drawable.compose_multiplatform), null)
            }
        }
    }
}
