package ui.auth.totp.viewmodel

import klib.data.auth.otp.model.TotpConfig
import kotlin.time.Duration

public data class TotpState(
    val code: String = "",
    val countdown: Duration = TotpConfig.DEFAULT.period,
    val error: String? = null,
)
