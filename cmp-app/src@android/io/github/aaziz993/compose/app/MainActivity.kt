package io.github.aaziz993.compose.app
import gradle.accessors.files
import gradle.api.trySet
import gradle.api.tryAddAll
import gradle.api.trySet

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

public class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
public fun AppAndroidPreview() {
    App()
}
