package presentation.components.country

import androidx.compose.runtime.Composable
import clib.presentation.components.country.CountryPickerDialog
import clib.presentation.components.country.model.CountryPicker
import compose_app.generated.resources.Res
import compose_app.generated.resources.language
import compose_app.generated.resources.search
import data.type.primitives.string.asStringResource
import klib.data.location.locale.Locale
import klib.data.type.collections.bimap.toBiMap
import org.jetbrains.compose.resources.stringResource

@Composable
public fun LocalePickerDialog(
    locales: List<Locale>,
    onLocaleChange: (Locale) -> Unit,
    onClose: () -> Unit,
) {
    val localeCountries = locales.associateWith { locale ->
        locale.country()!!.copy(
            name = locale.toString().asStringResource(),
        )
    }.toBiMap()

    CountryPickerDialog(
        onItemClicked = { country ->
            onLocaleChange(localeCountries.inverse[country]!!)
            onClose()
        },
        onDismissRequest = onClose,
        countries = localeCountries.values.toList(),
        picker = CountryPicker(
            headerTitle = stringResource(Res.string.language),
            searchHint = stringResource(Res.string.search),
        ),
    )
}
