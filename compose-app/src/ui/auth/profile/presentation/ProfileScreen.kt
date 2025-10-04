package ui.auth.profile.presentation

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.model.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.NavRoute
import ui.navigation.presentation.Profile

@Composable
public fun ProfileScreen(
    route: Profile,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewProfileScreen(): Unit = ProfileScreen(Profile)
