package clib.presentation.components.dialog.password.model

public data class PasswordResetDialog(
    val title: String = "Reset Password",
    val password: String = "Password",
    val newPassword: String = "New Password",
    val repeatPassword: String = "Repeat password",
    val passwordMismatch: String = "Passwords mismatch",
    val submit: String = "Submit",
)
