package clib.presentation.locale.stateholder

import klib.data.location.locale.Locale

public sealed interface LocaleAction {
    public data class SetLocale(val value: Locale?) : LocaleAction
}

