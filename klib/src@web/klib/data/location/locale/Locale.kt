package klib.data.location.locale

private typealias WebLocale = Intl.Locale

public fun WebLocale.toKotlinLocale(): Locale = Locale.forLanguageTag(toString())

public fun Locale.toWebLocale(): WebLocale = WebLocale(toLanguageTag().toString())
