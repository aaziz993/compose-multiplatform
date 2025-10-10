package ui.auth.pincode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.navigation.viewmodel.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.confirm
import compose_app.generated.resources.pin_code
import compose_app.generated.resources.repeat_pin_code
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.components.textfield.otp.AppOtpInputField
import ui.auth.pincode.viewmodel.PinCodeAction
import ui.auth.pincode.viewmodel.PinCodeState
import ui.navigation.presentation.PinCode

@Composable
public fun PinCodeScreen(
    modifier: Modifier = Modifier,
    route: PinCode = PinCode,
    state: PinCodeState = PinCodeState(),
    onAction: (PinCodeAction) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    val pinCode = remember(state.pinCode) { mutableStateOf(state.pinCode) }

    LaunchedEffect(pinCode.value) {
        if (pinCode.value != state.pinCode) onAction(PinCodeAction.SetPinCode(pinCode.value))
    }

    val repeatPinCode = remember(state.repeatPinCode) { mutableStateOf(state.repeatPinCode) }

    LaunchedEffect(repeatPinCode.value) {
        if (repeatPinCode.value != state.repeatPinCode) onAction(PinCodeAction.RepeatPinCode(repeatPinCode.value))
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.pin_code),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        AppOtpInputField(otp = pinCode)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.repeat_pin_code),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        AppOtpInputField(otp = repeatPinCode)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAction(PinCodeAction.Confirm) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.confirm))
        }
    }
}

@Preview
@Composable
public fun PreviewPinCodeScreen(): Unit = PinCodeScreen()


