package clib.presentation.components.picker.model

import androidx.compose.ui.graphics.Color

public data class Picker(
    var itemPadding: Int = 10,
    var dividerColor: Color = Color.LightGray,
    var headerTitle: String = "Select item",
    var searchHint: String = "Search item",
    var clear: String = "Clear",
    var showSearchClearIcon: Boolean = true,
)
