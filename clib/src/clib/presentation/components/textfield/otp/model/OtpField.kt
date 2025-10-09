package clib.presentation.components.textfield.otp.model

import androidx.compose.ui.focus.FocusRequester

/**
 * Data class representing an individual OTP field.
 *
 * @property text The text content of the OTP field.
 * @property focusRequester A FocusRequester to manage focus on the field.
 */
public data class OtpField(
    val text: String,
    val index: Int,
    val focusRequester: FocusRequester? = null
)
