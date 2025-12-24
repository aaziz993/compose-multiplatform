package ui.auth.hotp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.type.primitives.string.stringResource
import clib.presentation.icons.Otp
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.code_sent_to
import compose_app.generated.resources.hotp
import compose_app.generated.resources.send_code
import klib.data.auth.otp.model.OtpConfig
import klib.data.auth.otp.model.TotpConfig
import presentation.components.textfield.otp.OtpInputField
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
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
): Unit = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val otpValue = remember(state.code) { mutableStateOf(state.code) }

        LaunchedEffect(Unit) {
            onAction(HotpAction.SendNewCode)
        }

        LaunchedEffect(otpValue.value) {
            if (otpValue.value != state.code) onAction(HotpAction.SetCode(otpValue.value))
            if (otpValue.value.length == config.codeDigits) onAction(HotpAction.Confirm)
        }

        Icon(
            imageVector = Icons.Default.Otp,
            contentDescription = stringResource(Res.string.hotp),
            modifier = Modifier.size(128.dp),
        )

        Text(
            text = stringResource(Res.string.code_sent_to, route.contact),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
            overflow = TextOverflow.Clip,
            maxLines = 1,
        )

        Text("Test code-${testOtpCode}")

        OtpInputField(
            otp = otpValue,
            count = config.codeDigits,
        )

        Text(
            text = stringResource(Res.string.send_code),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onAction(HotpAction.SendNewCode)
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
