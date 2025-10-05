package klib.data.location.locale

import web.navigator.navigator

private typealias WebLocale = Intl.Locale

public fun WebLocale.toKotlinLocale(): Locale = Locale.forLanguageTag(toString())

public fun Locale.toWebLocale(): WebLocale = WebLocale(toLanguageTag().toString())

public actual val Locale.Companion.current: Locale
    get() = Locale.forLanguageTag(navigator.language)
