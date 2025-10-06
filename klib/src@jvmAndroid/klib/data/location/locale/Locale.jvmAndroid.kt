package klib.data.location.locale

import java.util.Locale as JavaLocale

public fun JavaLocale.toKotlinLocale(): Locale = Locale.forLanguageTag(toLanguageTag())

public fun Locale.toJavaLocale(): JavaLocale = JavaLocale.forLanguageTag(toLanguageTag().toString())

public actual val Locale.Companion.current: Locale
    get() = JavaLocale.getDefault().toKotlinLocale()

public actual fun Locale.Companion.setCurrent(locale: Locale?) {
    if (locale != null) JavaLocale.setDefault(locale.toJavaLocale())
}
