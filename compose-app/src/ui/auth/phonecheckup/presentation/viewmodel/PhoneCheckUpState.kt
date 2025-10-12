package ui.auth.phonecheckup.presentation.viewmodel

public data class PhoneCheckUpState(
    val countryCode: String = "",
    val number: String = "",
    val isValid: Boolean = false,
)
