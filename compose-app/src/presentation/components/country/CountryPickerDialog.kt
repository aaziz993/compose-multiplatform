package presentation.components.country

import androidx.compose.runtime.Composable
import clib.presentation.components.country.CountryPickerDialog
import clib.presentation.components.country.model.CountryPicker
import compose_app.generated.resources.Res
import compose_app.generated.resources.language
import compose_app.generated.resources.search
import data.type.primitives.string.asStringResource
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import klib.data.location.locale.Locale
import org.jetbrains.compose.resources.stringResource

@Composable
public fun LocalePickerDialog(
    onLocaleChange: (Locale) -> Unit,
    onClose: () -> Unit,
): Unit = CountryPickerDialog(
    onItemClicked = { country ->
        country.locales().firstOrNull()?.let { locale ->
            onLocaleChange(locale)
            onClose()
        }
    },
    onDismissRequest = onClose,
    countries = Country.getCountries()
        .filter { country -> country.locales().isNotEmpty() }
        .toList()
        .map { country ->
            country.copy(
                name = country.toString().asStringResource { country.name },
            )
        },
    picker = CountryPicker(
        headerTitle = stringResource(Res.string.language),
        searchHint = stringResource(Res.string.search),
    ),
)
