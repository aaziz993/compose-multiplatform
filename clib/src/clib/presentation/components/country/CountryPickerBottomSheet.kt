package clib.presentation.components.country

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import clib.presentation.components.country.model.CountryPicker
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview

@Composable
public fun CountryPickerBottomSheet(
    onItemClicked: (item: Country) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    countries: List<Country> = Country.getCountries().toList(),
    textStyle: TextStyle = TextStyle(),
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: CountryPicker = CountryPicker(),
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var value by remember { mutableStateOf("") }

    val filteredCountries by remember(value) {
        derivedStateOf {
            if (value.isEmpty()) countries else countries.searchCountry(value)
        }

    }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
    ) {
        Surface(
            color = backgroundColor, modifier = modifier,
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.height(itemPadding.dp))

                CountryHeaderSheet(title = picker.headerTitle)

                Spacer(modifier = Modifier.height(itemPadding.dp))

                CountrySearch(
                    value = value,
                    onValueChange = { value = it },
                    textStyle = textStyle,
                    hint = picker.searchHint,
                    showClearIcon = picker.showSearchClearIcon,
                    requestFocus = false,
                    onFocusChanged = {
                        if (it.hasFocus) {
                            scope.launch {
                                sheetState.expand()
                            }
                        }

                    },
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
private fun PreviewCountryPickerBottomSheet(): Unit =
    CountryPickerBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        onDismissRequest = {},
        onItemClicked = {},
    )
