package ui.auth.login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.components.textfield.AdvancedTextField
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.auth.login.presentation.viewmodel.LoginAction
import ui.auth.login.presentation.viewmodel.LoginState
import ui.navigation.presentation.ForgotPassword
import ui.navigation.presentation.Home
import ui.navigation.presentation.Login

@Composable
public fun LoginScreen(
    route: Login,
    state: LoginState = LoginState(),
    action: (LoginAction) -> Unit = {},
    navigationAction: (NavigationAction) -> Unit = {},
) {
    if (state.user != null) navigationAction(NavigationAction.TypeSafeNavigation.NavigateAndClearCurrent(Home))

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        AdvancedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.username,
            onValueChange = { value -> action(LoginAction.SetUsername(value)) },
            label = { Text("Username") },
            isError = state.isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            outlined = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        AdvancedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = { value -> action(LoginAction.SetPassword(value)) },
            label = { Text("Password") },
            isError = state.isError,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            outlined = true,
            showValue = state.showPassword,
            onShowValueChange = { value -> action(LoginAction.ShowPassword(value)) },
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Forgot password?",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navigationAction(NavigationAction.TypeSafeNavigation.Navigate(ForgotPassword(state.username)))
                }
                .padding(vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { action(LoginAction.Login) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Log in")
        }
    }
}

@Preview
@Composable
public fun PreviewLoginScreen(): Unit = LoginScreen(Login)
