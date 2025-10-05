package klib.data.location.locale

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier

public fun NSLocale.toKotlinLocale(): Locale {
    val tag = localeIdentifier.replace('_', '-')

    return Locale.forLanguageTag(
        when {
            tag.isEmpty() -> LanguageTag.undeterminedPrefix
            tag.startsWith("-") -> "${LanguageTag.undeterminedPrefix}$tag"
            else -> tag
        },
    )
}

public fun Locale.toNSLocale(): NSLocale {
    val tag = toLanguageTag().toString()

    return NSLocale(
        when {
            tag == LanguageTag.undeterminedPrefix -> ""
            tag.startsWith("${LanguageTag.undeterminedPrefix}-") -> tag.removePrefix(LanguageTag.undeterminedPrefix)
            else -> tag
        },
    )
}

public actual val Locale.Companion.current: Locale
    get() = NSLocale.currentLocale.toKotlinLocale()

