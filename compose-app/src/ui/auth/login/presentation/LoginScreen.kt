package ui.auth.login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.navigation.NavigationAction
import clib.presentation.components.textfield.AdvancedTextField
import compose_app.generated.resources.Res
import compose_app.generated.resources.login
import compose_app.generated.resources.pin_code
import compose_app.generated.resources.reset_password
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ui.auth.login.presentation.viewmodel.LoginAction
import ui.auth.login.presentation.viewmodel.LoginState
import ui.navigation.presentation.Login
import ui.navigation.presentation.Phone

@Composable
public fun LoginScreen(
    modifier: Modifier = Modifier,
    route: Login = Login,
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.login),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        AdvancedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.pinCode,
            onValueChange = { value -> onAction(LoginAction.SetPinCode(value)) },
            label = { Text(text = stringResource(Res.string.pin_code)) },
            isError = state.error != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next,
            ),
            singleLine = true,
            outlined = true,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(Res.string.reset_password),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onNavigationAction(NavigationAction.Push(Phone))
                }
                .padding(vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAction(LoginAction.Login) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.login))
        }
    }
}

@Preview
@Composable
public fun PreviewLoginScreen(): Unit = LoginScreen()
