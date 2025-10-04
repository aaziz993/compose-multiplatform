package ui.about

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.model.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.About
import ui.navigation.presentation.NavRoute

@Composable
public fun AboutScreen(
    route: About,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewAboutScreen(): Unit = AboutScreen(About)
