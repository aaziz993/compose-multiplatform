package ui.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.navigation.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.ForgotPinCode

@Composable
public fun ForgotPinCodeScreen(
    modifier: Modifier = Modifier,
    route: ForgotPinCode = ForgotPinCode,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
private fun PreviewForgotPinCodeScreen(): Unit = ForgotPinCodeScreen()
