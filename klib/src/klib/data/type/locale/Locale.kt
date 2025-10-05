//package klib.data.type.locale
//
//import io.fluidsonic.locale.Locale
//
//public expect val CURRENT_LOCALE: Locale
//
//public fun String.toLocale(delimiter: String = "-"): Locale =
//    Locale.forLanguage(substringBefore(delimiter), region = substringAfter(delimiter, "").ifEmpty { null })
//
//public fun String.toLocaleOrNull(delimiter: String = "-"): Locale? =
//    Locale.forLanguageOrNull(substringBefore(delimiter), region = substringAfter(delimiter, ""))
//
