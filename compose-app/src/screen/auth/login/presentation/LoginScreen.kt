package screen.auth.login.presentation

import androidx.compose.runtime.Composable
import screen.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
public fun LoginScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {

}

@Preview
@Composable
public fun previewLoginScreen() {
    LoginScreen()
}
