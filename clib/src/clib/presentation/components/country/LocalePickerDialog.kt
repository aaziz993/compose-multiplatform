package clib.presentation.components.country

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.presentation.components.country.model.CountryPicker
import klib.data.location.locale.Locale
import klib.data.type.collections.bimap.toBiMap

@Composable
public fun LocalePickerDialog(
    onItemClicked: (item: Locale) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    locales: List<Locale> = Locale.getLocales().toList(),
    textStyle: TextStyle = TextStyle(),
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: CountryPicker = CountryPicker(),
) {
    val localeCountryMap by remember(locales) {
        derivedStateOf {
            locales.associateWith { locale -> locale.country()!!.copy(name = locale.toString()) }.toBiMap()
        }
    }

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
public fun LocalePickerDialogPreview(): Unit =
    LocalePickerDialog(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp)),
        onDismissRequest = {},
        onItemClicked = {},
    )
