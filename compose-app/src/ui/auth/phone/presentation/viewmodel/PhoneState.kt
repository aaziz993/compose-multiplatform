package ui.auth.phone.presentation.viewmodel

public data class PhoneState(
    val dial: String = "",
    val number: String = "",
    val isValid: Boolean = false,
)
