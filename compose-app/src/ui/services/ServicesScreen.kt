package ui.services

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Home
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
public fun PreviewServicesScreen(): Unit = ServicesScreen()
