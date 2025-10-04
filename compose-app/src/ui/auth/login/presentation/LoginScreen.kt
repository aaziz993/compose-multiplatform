package ui.auth.login.presentation

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Login
import ui.navigation.presentation.NavRoute

@Composable
public fun LoginScreen(
    route: Login,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewLoginScreen(): Unit = LoginScreen(Login)
