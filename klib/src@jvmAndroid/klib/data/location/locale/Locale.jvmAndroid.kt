package klib.data.location.locale

import java.util.Locale as JavaLocale

public fun JavaLocale.toKotlinLocale(): Locale = Locale.forLanguageTag(toLanguageTag())

public fun Locale.toJavaLocale(): JavaLocale = JavaLocale.forLanguageTag(languageTag.toString())
