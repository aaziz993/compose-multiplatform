package clib.presentation.locale.viewmodel

import klib.data.location.locale.Locale

public sealed interface LocaleAction {
    public data class SetLocale(val value: Locale?) : LocaleAction
}
