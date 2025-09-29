package ui.about

import androidx.compose.runtime.Composable
import ui.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun AboutScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewAboutScreen(): Unit = AboutScreen()
