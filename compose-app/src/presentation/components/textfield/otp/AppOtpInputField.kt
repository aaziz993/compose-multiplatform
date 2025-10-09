package presentation.components.textfield.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.textfield.otp.OtpInputField

@Composable
public fun AppOtpInputField(
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
