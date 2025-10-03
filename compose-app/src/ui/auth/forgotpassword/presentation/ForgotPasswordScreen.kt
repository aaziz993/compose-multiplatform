package ui.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import ui.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun ForgotPasswordScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewForgotPasswordScreen(): Unit = ForgotPasswordScreen()
