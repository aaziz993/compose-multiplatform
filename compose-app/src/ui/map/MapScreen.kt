package ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Map
import ui.navigation.presentation.NavRoute

@Composable
public fun MapScreen(
    modifier: Modifier = Modifier,
    route: Map = Map,
    navigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewMapScreen(): Unit = MapScreen()
