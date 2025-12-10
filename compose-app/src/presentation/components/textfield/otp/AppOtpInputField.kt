package presentation.components.textfield.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.textfield.otp.OtpInputField

@Suppress("ComposeModifierMissing")
@Composable
public fun AppOtpInputField(
    otp: MutableState<String>,
    count: Int = 4,
    enabled: Boolean = true,
    otpTextType: KeyboardType = KeyboardType.Number,
    textColor: Color = MaterialTheme.colorScheme.primary,
    maxWidth: Dp = 400.dp,          // maximum total width.
    spacing: Dp = 8.dp,             // spacing between boxes.
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
            enabled = enabled,
            otpBoxModifier = Modifier
                .size(boxSize)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)),
            otpTextType = otpTextType,
            textColor = textColor,
        )
    }
}
