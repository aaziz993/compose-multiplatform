package ui.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.stateholder.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.About

@Composable
public fun AboutScreen(
    modifier: Modifier = Modifier,
    route: About = About,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewAboutScreen(): Unit = AboutScreen()
