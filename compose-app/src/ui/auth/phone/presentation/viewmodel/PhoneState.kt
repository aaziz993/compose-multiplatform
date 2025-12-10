package ui.auth.phone.presentation.viewmodel

public data class PhoneState(
    val countryCode: String = "",
    val phone: String = "",
    val isValid: Boolean = false,
)
