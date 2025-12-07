package clib.presentation.components.country

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.presentation.components.country.model.CountryPicker
import klib.data.location.country.Country
import klib.data.location.locale.Locale
import klib.data.type.collections.bimap.toBiMap

@Composable
public fun LocalePickerDialog(
    onItemClicked: (item: Locale) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    locales: List<Locale> = Locale.getLocales().toList(),
    country: @Composable (Locale) -> Country = { locale -> locale.country()!! },
    textStyle: TextStyle = LocalTextStyle.current,
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: CountryPicker = CountryPicker(),
) {
    val localeCountryMap = locales.associateWith{country(it)}.toBiMap()

    CountryPickerDialog(
        { country -> onItemClicked(localeCountryMap.inverse[country]!!) },
        onDismissRequest,
        modifier,
        localeCountryMap.values.toList(),
        textStyle,
        itemPadding,
        backgroundColor,
        picker,
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewLocalePickerDialog(): Unit =
    LocalePickerDialog(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp)),
        onDismissRequest = {},
        onItemClicked = {},
    )
