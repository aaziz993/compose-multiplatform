package clib.presentation.components.country

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import clib.presentation.components.country.model.CountryPicker
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import androidx.compose.ui.tooling.preview.Preview

@Composable
public fun CountryPickerDialog(
    onItemClicked: (item: Country) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    countries: List<Country> = Country.getCountries().toList(),
    textStyle: TextStyle = TextStyle(),
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: CountryPicker = CountryPicker()
) {
    var value by remember { mutableStateOf("") }

    val filteredCountries by remember(value) {
        derivedStateOf {
            if (value.isEmpty()) countries else countries.searchCountry(value)
        }
    }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            color = backgroundColor, modifier = modifier,
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.height(itemPadding.dp))

                CountryHeaderDialog(
                    title = picker.headerTitle,
                    onDismiss = { onDismissRequest() },
                )

                Spacer(modifier = Modifier.height(itemPadding.dp))

                CountrySearch(
                    value = value,
                    onValueChange = { value = it },
                    textStyle = textStyle,
                    hint = picker.searchHint,
                    showClearIcon = picker.showSearchClearIcon,
                )

                Spacer(modifier = Modifier.height(itemPadding.dp))


                LazyColumn {
                    items(filteredCountries, key = { it.name }) { countryItem ->
                        HorizontalDivider(color = picker.dividerColor)
                        CountryUI(
                            country = countryItem,
                            onCountryClicked = { onItemClicked(countryItem) },
                            countryTextStyle = textStyle,
                            itemPadding = itemPadding,
                            showCountryIso = picker.showCountryIso,
                            showCountryDial = picker.showCountryCode,
                            showCountryFlag = picker.showFlag,
                        )
                    }

                }

            }

        }

    }
}

@Preview(showBackground = true)
@Composable
public fun CountryDialogPreview(): Unit =
    CountryPickerDialog(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp)),
        countries = Country.getCountries().toList(),
        onDismissRequest = {},
        onItemClicked = {},
    )

/**
 * Search country by query
 * @param query String
 * @return List<Countries>
 */
public fun List<Country>.searchCountry(query: String): List<Country> {
    val normalizedQuery = query.trim()
    return filter { country ->
        country.toString().contains(normalizedQuery, true) || country.name.contains(
            normalizedQuery,
            true,
        ) || country.dial?.contains(normalizedQuery, true) == true
    }
}

