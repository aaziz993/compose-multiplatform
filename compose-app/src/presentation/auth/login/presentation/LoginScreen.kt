package presentation.auth.login.presentation

import androidx.compose.runtime.Composable
import presentation.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun LoginScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewLoginScreen(): Unit = LoginScreen()
