package ui.auth.otp.viewmodel

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public const val OTP_CODE_LENGTH: Int = 4

public data class OtpState(
    val code: String = "",
    val duration: Duration = 60.seconds,
    val error: String? = null,
)
