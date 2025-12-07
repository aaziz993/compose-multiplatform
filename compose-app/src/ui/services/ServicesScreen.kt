package ui.services

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.navigation.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Services

@Composable
public fun ServicesScreen(
    modifier: Modifier = Modifier,
    route: Services = Services,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
private fun PreviewServicesScreen(): Unit = ServicesScreen()
