package ui.auth.otp.viewmodel

public data class OtpState(
    val code: String = "",
    val confirmed: Boolean = false,
    val error: String? = null,
)
