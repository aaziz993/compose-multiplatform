package klib.data.location.locale

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.localeIdentifier
import platform.Foundation.regionCode
import platform.Foundation.scriptCode

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

public actual fun currentLocale(): Locale = NSLocale.currentLocale.toKotlinLocale()

