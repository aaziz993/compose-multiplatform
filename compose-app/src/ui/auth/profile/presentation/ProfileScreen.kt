package ui.auth.profile.presentation

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.NavRoute
import ui.navigation.presentation.Profile

@Composable
public fun ProfileScreen(
    route: Profile,
    navigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewProfileScreen(): Unit = ProfileScreen(Profile)
