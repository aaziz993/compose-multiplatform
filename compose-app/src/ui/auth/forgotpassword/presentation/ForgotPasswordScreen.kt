package ui.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.ForgotPassword
import ui.navigation.presentation.NavRoute

@Composable
public fun ForgotPasswordScreen(
    route: ForgotPassword,
    navigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewForgotPasswordScreen(): Unit = ForgotPasswordScreen(ForgotPassword(""))
