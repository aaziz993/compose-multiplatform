package ui.auth.hotp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.icons.Otp
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.code_sent_to
import compose_app.generated.resources.generate
import compose_app.generated.resources.otp
import klib.data.auth.otp.model.OtpConfig
import klib.data.auth.otp.model.TotpConfig
import org.jetbrains.compose.resources.stringResource
import presentation.components.textfield.otp.AppOtpInputField
import ui.auth.hotp.viewmodel.HotpAction
import ui.auth.hotp.viewmodel.HotpState
import ui.navigation.presentation.Hotp

public var testOtpCode: String = ""

@Composable
public fun HotpScreen(
    modifier: Modifier = Modifier,
    route: Hotp = Hotp(),
    config: OtpConfig = TotpConfig.DEFAULT,
    state: HotpState = HotpState(),
    onAction: (HotpAction) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    val otpValue = remember(state.code) { mutableStateOf(state.code) }

    LaunchedEffect(Unit) {
        onAction(HotpAction.GenerateCode)
    }

    LaunchedEffect(otpValue.value) {
        if (otpValue.value != state.code) onAction(HotpAction.SetCode(otpValue.value))
        if (otpValue.value.length == config.codeDigits) onAction(HotpAction.Confirm)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Otp,
            contentDescription = stringResource(Res.string.otp),
            modifier = Modifier.size(128.dp),
        )

        Text(
            text = stringResource(Res.string.code_sent_to, route.contact),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        Text("Test code-${testOtpCode}")

        AppOtpInputField(
            otp = otpValue,
            count = config.codeDigits,
        )

        Text(
            text = stringResource(Res.string.generate),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onAction(HotpAction.GenerateCode)
                }
                .padding(vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview
@Composable
private fun PreviewHotpScreen(): Unit = HotpScreen()
