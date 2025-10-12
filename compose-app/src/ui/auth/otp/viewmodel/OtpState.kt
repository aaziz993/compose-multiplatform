package ui.auth.otp.viewmodel

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public const val OTP_CODE_LENGTH: Int = 4

public val OTP_DURATION: Duration = 60.seconds

public data class OtpState(
    val code: String = "",
    val countdown: Duration = OTP_DURATION,
    val error: String? = null,
)
