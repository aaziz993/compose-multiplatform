import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import clib.di.KoinApplicationPreview
import clib.di.KoinMultiplatformApplication
import di.koinConfiguration
import org.koin.dsl.KoinConfiguration
import presentation.AppComposable

@Composable
public fun App(): Unit = KoinMultiplatformApplication(KoinConfiguration(koinConfiguration())) {
    AppComposable()
}

@Preview
@Composable
public fun PreviewApp(): Unit = KoinApplicationPreview(koinConfiguration()) {
    AppComposable()
}
