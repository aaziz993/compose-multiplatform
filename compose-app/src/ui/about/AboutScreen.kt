package ui.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.About
import ui.navigation.presentation.NavRoute

@Composable
public fun AboutScreen(
    modifier: Modifier = Modifier,
    route: About = About,
    navigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewAboutScreen(): Unit = AboutScreen()
