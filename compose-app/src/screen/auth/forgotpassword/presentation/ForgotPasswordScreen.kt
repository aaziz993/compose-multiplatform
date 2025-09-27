package screen.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
public fun ForgotPasswordScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {

}

@Preview
@Composable
public fun previewForgotPasswordScreen() {
    ForgotPasswordScreen()
}
