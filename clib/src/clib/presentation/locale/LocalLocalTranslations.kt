package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.staticCompositionLocalOf
import klib.data.location.locale.LocaleService
import klib.data.location.locale.Localization

@Suppress("ComposeCompositionLocalUsage")
public val LocalLocalization: ProvidableCompositionLocal<Localization> =
    staticCompositionLocalOf(::Localization)

@Composable
public fun rememberLocaleService(
    localeState: LocaleState,
    localeService: LocaleService
): State<Localization> =
    produceState(initialValue = Localization(), localeState, localeState.locale) {
        value = localeService.getLocale(localeState.locale)
    }
