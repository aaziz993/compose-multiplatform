package ui.settings

import androidx.compose.runtime.Composable
import ui.navigation.presentation.Route
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun SettingsScreen(
    navigateTo: (route: Route) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewSettingsScreen(): Unit = SettingsScreen()
