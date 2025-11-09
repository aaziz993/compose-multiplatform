package ui.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.stateholder.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.ForgotPassword

@Composable
public fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    route: ForgotPassword = ForgotPassword,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {

}

@Preview
@Composable
public fun PreviewForgotPasswordScreen(): Unit = ForgotPasswordScreen()
