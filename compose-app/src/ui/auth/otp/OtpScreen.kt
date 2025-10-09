package ui.auth.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.navigation.model.NavigationDestination
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.components.textfield.otp.OtpInputField
import clib.presentation.theme.density.pxToDp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Home
import ui.navigation.presentation.Login
import ui.navigation.presentation.Otp
import ui.auth.otp.viewmodel.OtpAction
import ui.auth.otp.viewmodel.OtpState

@Composable
public fun OtpScreen(
    modifier: Modifier = Modifier,
    route: Otp = Otp(),
    state: OtpState = OtpState(),
    action: (OtpAction) -> Unit = {},
    loggedInDestination: NavigationDestination<*> = Home,
    navigationAction: (NavigationAction) -> Unit = {},
) {
    if (state.confirmed) navigationAction(NavigationAction.TypeSafeNavigation.NavigateAndClearCurrent(Login))

    val otpValue = remember(state.code) { mutableStateOf(state.code) }

    LaunchedEffect(otpValue.value) {
        if (otpValue.value != state.code) {
            action(OtpAction.SetCode(otpValue.value))
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Enter code sent to ${route.phone}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        OtpInputField(
            otp = otpValue,
            count = 4,
            otpBoxModifier = Modifier
                .border(1.pxToDp(), Color.Black)
                .background(Color.White),
            otpTextType = KeyboardType.Number,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Send code again?",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    action(OtpAction.SendCode)
                }
                .padding(vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview
@Composable
public fun PreviewOtpScreen(): Unit = OtpScreen()
