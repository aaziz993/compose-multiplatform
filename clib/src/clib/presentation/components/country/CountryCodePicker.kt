package clib.presentation.components.country

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.country.model.CountryView
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import androidx.compose.ui.tooling.preview.Preview

@Composable
public fun CountryCodePicker(
    onCountrySelected: (Country) -> Unit,
    modifier: Modifier = Modifier,
    selectedCountry: Country = Country.getCountries().first(),
    countries: List<Country> = Country.getCountries().toList(),
    view: CountryView = CountryView(),
    picker: CountryPicker = CountryPicker(),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    textStyle: TextStyle = TextStyle(),
    showSheet: Boolean = false,
    itemPadding: Int = 10,
) {

    var country by remember { mutableStateOf(selectedCountry) }
    var isPickerOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.clickable {
            isPickerOpen = true
        },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        CountryView(
            country = country,
            showFlag = view.showFlag,
            showCountryIso = view.showCountryIso,
            showCountryName = view.showCountryName,
            showCountryCode = view.showCountryCode,
            showArrow = view.showArrow,
            textStyle = textStyle,
            itemPadding = itemPadding,
            clipToFull = view.clipToFull,
        )

        if (isPickerOpen) {
            if (showSheet) {
                CountryPickerBottomSheet(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f),
                    onDismissRequest = { isPickerOpen = false },
                    onItemClicked = {
                        onCountrySelected(it)
                        country = it
                        isPickerOpen = false

                    },
                    textStyle = textStyle,
                    countries = countries,
                    picker = picker,
                    itemPadding = itemPadding,
                    backgroundColor = backgroundColor,
                )
            }
            else {
                CountryPickerDialog(
                    modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
                    onDismissRequest = { isPickerOpen = false },
                    onItemClicked = {
                        onCountrySelected(it)
                        country = it
                        isPickerOpen = false

                    },
                    textStyle = textStyle,
                    countries = countries,
                    picker = picker,
                    itemPadding = itemPadding,
                    backgroundColor = backgroundColor,
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
public fun CountryCodePickerPreview(): Unit = CountryCodePicker(onCountrySelected = {})
