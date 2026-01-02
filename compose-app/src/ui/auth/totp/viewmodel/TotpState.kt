package ui.auth.totp.viewmodel

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public data class TotpState(
    val code: String = "",
    val countdown: Duration = 30.seconds,
    val error: String? = null,
)
