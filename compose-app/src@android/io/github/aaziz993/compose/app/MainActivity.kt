package io.github.aaziz993.compose.app

import Screen
import android.os.Bundle
import io.github.aaziz993.compose_app.generated.resources.Res
import io.github.aaziz993.compose_app.generated.resources.compose_multiplatform

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
