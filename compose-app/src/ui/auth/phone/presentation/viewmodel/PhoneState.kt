package ui.auth.phone.presentation.viewmodel

import klib.data.location.Phone

public data class PhoneState(
    val phone: Phone = Phone("", ""),
    val isValid: Boolean = false,
)
