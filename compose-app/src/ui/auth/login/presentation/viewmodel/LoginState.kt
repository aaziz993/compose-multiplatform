package ui.auth.login.presentation.viewmodel

public data class LoginState(
    val username: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val remember: Boolean = true,
    val error: Throwable? = null,
)
