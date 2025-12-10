package ui.auth.hotp.viewmodel

public data class HotpState(
    val code: String = "",
    val error: String? = null,
)
