package clib.presentation.components.country.model

public data class CountryView(
    var showFlag: Boolean = true,
    var showCountryIso: Boolean = false,
    var showCountryName: Boolean = false,
    var showCountryCode: Boolean = true,
    var showArrow: Boolean = true,
    var clipToFull: Boolean = false,
)
