package screen.about

import androidx.compose.runtime.Composable
import screen.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
public fun AboutScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {

}

@Preview
@Composable
public fun previewAboutScreen() {
    AboutScreen()
}
