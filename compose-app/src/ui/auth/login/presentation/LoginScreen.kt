package ui.auth.login.presentation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.model.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Login
import ui.navigation.presentation.NavRoute

@Composable
public fun LoginScreen(
    route: Login,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    Button({
//        navigateTo(ForgotPassword("aziz"))
    }){
        Text("Forgot")
    }
}

@Preview
@Composable
public fun PreviewLoginScreen(): Unit = LoginScreen(Login)
