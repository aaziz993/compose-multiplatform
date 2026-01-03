package presentation.components.textfield.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.textfield.otp.OtpInputField

@Suppress("ComposeModifierMissing")
@Composable
public fun OtpInputField(
    otp: MutableState<String>,
    count: Int = 4,
    enabled: Boolean = true,
    otpTextType: KeyboardType = KeyboardType.Number,
    textColor: Color = MaterialTheme.colorScheme.primary,
    maxWidth: Dp = 480.dp,
): Unit = OtpInputField(
    otp = otp,
    modifier = Modifier.widthIn(max = maxWidth),
    otpBoxModifier = Modifier
        .padding(4.dp)
        .clip(RoundedCornerShape(8.dp))
        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)),
    count = count,
    enabled = enabled,
    otpTextType = otpTextType,
    textColor = textColor,
)
