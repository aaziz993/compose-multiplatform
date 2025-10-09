package ui.auth.phonecheckup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.components.textfield.AdvancedTextField
import compose_app.generated.resources.Res
import compose_app.generated.resources.phone
import compose_app.generated.resources.pin_code
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.auth.phonecheckup.presentation.viewmodel.PhoneCheckUpAction
import ui.auth.phonecheckup.presentation.viewmodel.PhoneCheckUpState
import ui.navigation.presentation.Otp
import ui.navigation.presentation.PhoneCheckUp

@Composable
public fun PhoneCheckUpScreen(
    modifier: Modifier = Modifier,
    route: PhoneCheckUp = PhoneCheckUp,
    state: PhoneCheckUpState = PhoneCheckUpState(),
    action: (PhoneCheckUpAction) -> Unit = {},
    navigationAction: (NavigationAction) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.phone),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        AdvancedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.phone,
            onValueChange = { value -> action(PhoneCheckUpAction.SetPhone(value)) },
            label = { Text(text = stringResource(Res.string.phone)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    navigationAction(
                        NavigationAction.TypeSafeNavigation.NavigateAndClearCurrent(Otp(state.phone)),
                    )
                },
            ),
            singleLine = true,
            outlined = true,
        )
    }
}

@Preview
@Composable
public fun PreviewPhoneConfirmScreen(): Unit = PhoneCheckUpScreen()
