package ui.map

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Map
import ui.navigation.presentation.NavRoute

@Composable
public fun MapScreen(
    route: Map,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewMapScreen(): Unit = MapScreen(Map)
