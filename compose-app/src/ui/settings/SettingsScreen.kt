package ui.settings

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.NavRoute
import ui.navigation.presentation.Settings

@Composable
public fun SettingsScreen(
    route: Settings,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewSettingsScreen(): Unit = SettingsScreen(Settings)
