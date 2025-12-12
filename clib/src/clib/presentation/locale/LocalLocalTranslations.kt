package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.staticCompositionLocalOf
import klib.data.location.locale.LocaleService

@Suppress("ComposeCompositionLocalUsage")
public val LocalLocaleServiceTranslations: ProvidableCompositionLocal<Map<String, List<String>>> =
    staticCompositionLocalOf(::emptyMap)

@Composable
public fun rememberLocaleServiceTranslations(
    localeState: LocaleState,
    localeService: LocaleService
): State<Map<String, List<String>>> = produceState(initialValue = emptyMap(), localeState) {
    value = localeService.getTranslations(localeState.locale)
}
