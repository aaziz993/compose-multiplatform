package ui.auth.login.presentation

import androidx.compose.runtime.Composable
import ui.navigation.presentation.Route
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun LoginScreen(
    navigateTo: (route: Route) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewLoginScreen(): Unit = LoginScreen()
