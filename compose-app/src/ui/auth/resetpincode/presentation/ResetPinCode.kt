package ui.auth.resetpincode.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.navigation.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.ResetPinCode

@Composable
public fun ResetPinCodeScreen(
    modifier: Modifier = Modifier,
    route: ResetPinCode = ResetPinCode,
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
) {
}

@Preview
@Composable
private fun PreviewForgotPinCodeScreen(): Unit = ResetPinCodeScreen()
