package ui.auth.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.icons.Otp
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.code_sent_to
import compose_app.generated.resources.otp
import compose_app.generated.resources.resend_code
import data.type.primitives.string.humanreadable.toRelativeHumanReadable
import klib.data.auth.otp.model.HotpConfig
import klib.data.auth.otp.model.OtpConfig
import klib.data.auth.otp.model.TotpConfig
import kotlin.time.Duration
import org.jetbrains.compose.resources.stringResource
import presentation.components.textfield.otp.AppOtpInputField
import ui.auth.otp.viewmodel.OTP_CODE_LENGTH
import ui.auth.otp.viewmodel.OtpAction
import ui.auth.otp.viewmodel.OtpState
import ui.navigation.presentation.Otp

public var testOtpCode: String = ""

@Composable
public fun OtpScreen(
    modifier: Modifier = Modifier,
    route: Otp = Otp(),
    config: OtpConfig = TotpConfig.DEFAULT,
    state: OtpState = OtpState(),
    onAction: (OtpAction) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    val otpValue = remember(state.code) { mutableStateOf(state.code) }

    LaunchedEffect(otpValue.value) {
        if (otpValue.value != state.code) onAction(OtpAction.SetCode(otpValue.value))
        if (otpValue.value.length == OTP_CODE_LENGTH) onAction(OtpAction.Confirm)
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
            text = stringResource(Res.string.code_sent_to, route.phone),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        Text("Test code-$testOtpCode")

        when (config) {
            is TotpConfig -> {
                AppOtpInputField(
                    otp = otpValue,
                    count = config.codeDigits,
                    enabled = state.countdown > Duration.ZERO,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(
                        Res.string.resend_code,
                        if (state.countdown == Duration.ZERO) ""
                        else state.countdown.unaryMinus().toRelativeHumanReadable(),
                    ),
                    color = if (state.countdown == Duration.ZERO) MaterialTheme.colorScheme.primary else Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable(enabled = state.countdown == Duration.ZERO) {
                            onAction(OtpAction.ResendCode)
                        }
                        .padding(vertical = 8.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            is HotpConfig -> AppOtpInputField(
                otp = otpValue,
                count = config.codeDigits,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewOtpScreen(): Unit = OtpScreen()
