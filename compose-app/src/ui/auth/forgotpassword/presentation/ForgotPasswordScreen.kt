package ui.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.model.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.ForgotPassword
import ui.navigation.presentation.NavRoute

@Composable
public fun ForgotPasswordScreen(
    route: ForgotPassword,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewForgotPasswordScreen(): Unit = ForgotPasswordScreen(ForgotPassword(""))
