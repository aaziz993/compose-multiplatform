package clib.presentation.components.country.model

import androidx.compose.ui.graphics.Color

public data class CountryPicker(
  val itemPadding: Int = 10,
  val dividerColor: Color = Color.LightGray,
  val headerTitle: String = "Select Country",
  val searchHint: String = "Search Country",
  val clear: String = "Clear",
  val showSearchClearIcon: Boolean = true,
  val showCountryCode: Boolean = true,
  val showFlag: Boolean = true,
  val showCountryIso: Boolean = false,
)
