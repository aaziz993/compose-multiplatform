package ui.auth.login.presentation.viewmodel

public data class LoginState(
    val pinCode: String = "",
    val showPinCode: Boolean = false,
    val error: String? = null,
)
