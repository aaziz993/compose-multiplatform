package clib.presentation.components.picker.country.mode

import androidx.compose.ui.graphics.Color

public data class CountryPicker(
    var itemPadding: Int = 10,
    var dividerColor: Color = Color.LightGray,
    var headerTitle: String = "Select Country",
    var searchHint: String = "Search Country",
    var showSearchClearIcon: Boolean = true,
    var showCountryCode: Boolean = true,
    var showFlag: Boolean = true,
    var showCountryIso: Boolean = false,
)
