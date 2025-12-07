package clib.presentation.components.country.model

public data class CountryView(
    val showFlag: Boolean = true,
    val showCountryIso: Boolean = false,
    val showCountryName: Boolean = false,
    val showCountryCode: Boolean = true,
    val showArrow: Boolean = true,
    val clipToFull: Boolean = false,
)
