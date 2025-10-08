package ui.auth.login.presentation.viewmodel

import klib.data.type.auth.User

public data class LoginState(
    val username: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val user: User? = null,
    val error: String? = null,
)
