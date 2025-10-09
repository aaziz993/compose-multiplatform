package ui.auth.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.components.textfield.otp.OtpInputField
import compose_app.generated.resources.Res
import compose_app.generated.resources.enter_code_sent_to
import compose_app.generated.resources.resend_code
import compose_app.generated.resources.resend_in
import klib.data.type.primitives.string.humanreadable.toHumanReadable
import kotlin.time.Duration
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.auth.otp.viewmodel.OtpAction
import ui.auth.otp.viewmodel.OtpState
import ui.navigation.presentation.Otp

@Composable
public fun OtpScreen(
    modifier: Modifier = Modifier,
    route: Otp = Otp(),
    state: OtpState = OtpState(),
    action: (OtpAction) -> Unit = {},
    navigationAction: (NavigationAction) -> Unit = {},
) {
    val otpValue = remember(state.code) { mutableStateOf(state.code) }

    LaunchedEffect(otpValue.value) {
        if (otpValue.value != state.code) action(OtpAction.SetCode(otpValue.value))
        if (otpValue.value.length == 4) action(OtpAction.Confirm(route.phone))
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.enter_code_sent_to, route.phone),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        ResponsiveOtpInputField(otp = otpValue)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (state.timer == Duration.ZERO) stringResource(Res.string.resend_code)
            else stringResource(Res.string.resend_in, state.timer.toHumanReadable()),
            color = if (state.timer == Duration.ZERO) MaterialTheme.colorScheme.primary else Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(enabled = state.timer == Duration.ZERO) {
                    action(OtpAction.ResendCode)
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

@Composable
private fun ResponsiveOtpInputField(
    otp: MutableState<String>,
    count: Int = 4,
    otpTextType: KeyboardType = KeyboardType.Number,
    textColor: Color = Color.Black,
    maxWidth: Dp = 300.dp,          // maximum total width
    spacing: Dp = 8.dp,             // spacing between boxes
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .widthIn(max = maxWidth),
    ) {
        // Compute per-box size dynamically
        val totalSpacing = spacing * (count - 1)
        val boxSize = ((maxWidth - totalSpacing) / count).coerceAtLeast(40.dp)

        OtpInputField(
            otp = otp,
            count = count,
            otpBoxModifier = Modifier
                .size(boxSize)
                .border(1.dp, Color.Black)
                .background(Color.White),
            otpTextType = otpTextType,
            textColor = textColor,
        )
    }
}
