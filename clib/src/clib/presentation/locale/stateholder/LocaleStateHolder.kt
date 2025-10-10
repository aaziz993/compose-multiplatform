package clib.presentation.locale.stateholder

import klib.data.location.locale.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

public class LocaleStateHolder(locale: Locale? = null) {

    public val state: StateFlow<Locale?>
        field = MutableStateFlow(locale)

    public fun action(action: LocaleAction): Unit = when (action) {
        is LocaleAction.SetLocale -> setLocale(action.value)
    }

    private fun setLocale(value: Locale?) = state.update { value }
}
