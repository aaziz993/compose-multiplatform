package ui.auth.login.presentation.viewmodel

public data class LoginState(
    val username: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val isLogin: Boolean = false,
    val isError: Boolean = false,
)
