package ui.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.ForgotPassword

@Composable
public fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    route: ForgotPassword = ForgotPassword,
    navigationAction: (NavigationAction) -> Unit = {},
) {

}

@Preview
@Composable
public fun PreviewForgotPasswordScreen(): Unit = ForgotPasswordScreen()
