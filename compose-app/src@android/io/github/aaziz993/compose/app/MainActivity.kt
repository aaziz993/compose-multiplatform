package io.github.aaziz993.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import Screen
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

@Preview
@Composable
public fun AppAndroidPreview() {
    Screen()
}
