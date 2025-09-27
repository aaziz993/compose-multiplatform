package screen.map

import androidx.compose.runtime.Composable
import screen.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
public fun MapScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {

}

@Preview
@Composable
public fun previewMapScreen() {
    MapScreen()
}
