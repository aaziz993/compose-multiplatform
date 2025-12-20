package ui.auth.login.presentation.viewmodel

public data class LoginState(
    val username: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val remember: Boolean = false,
    val error: Throwable? = null,
)
