package klib.data.location.locale

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier

public fun NSLocale.toKotlinLocale(): Locale {
    val tag = localeIdentifier.replace('_', '-')

    return Locale.forLanguageTag(
        when {
            tag.isEmpty() -> LanguageTag.UNDETERMINED_PREFIX
            tag.startsWith("-") -> "${LanguageTag.UNDETERMINED_PREFIX}$tag"
            else -> tag
        },
    )
}

public fun Locale.toNSLocale(): NSLocale {
    val tag = languageTag.toString()

    return NSLocale(
        when {
            tag == LanguageTag.UNDETERMINED_PREFIX -> ""
            tag.startsWith("${LanguageTag.UNDETERMINED_PREFIX}-") -> tag.removePrefix(LanguageTag.UNDETERMINED_PREFIX)
            else -> tag
        },
    )
}

public actual val Locale.Companion.current: Locale
    get() = NSLocale.currentLocale.toKotlinLocale()

