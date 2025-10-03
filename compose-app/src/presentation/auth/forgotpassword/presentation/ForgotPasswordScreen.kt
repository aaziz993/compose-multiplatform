package presentation.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import presentation.navigation.presentation.Destination
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
