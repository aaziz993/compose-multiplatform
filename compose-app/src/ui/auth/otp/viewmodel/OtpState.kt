package ui.auth.otp.viewmodel

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public data class OtpState(
    val code: String = "",
    val timer: Duration = 60.seconds,
    val error: String? = null,
)
