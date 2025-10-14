package ui.auth.phone.presentation.viewmodel

public data class PhoneState(
    val countryCode: String = "",
    val number: String = "",
    val isValid: Boolean = false,
)
