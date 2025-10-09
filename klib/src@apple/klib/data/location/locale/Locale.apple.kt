package klib.data.location.locale

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier
import platform.Foundation.preferredLanguages

private const val LANG_KEY = "AppleLanguages"

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
    val tag = toLanguageTag().toString()

    return NSLocale(
        when {
            tag == LanguageTag.UNDETERMINED_PREFIX -> ""
            tag.startsWith("${LanguageTag.UNDETERMINED_PREFIX}-") -> tag.removePrefix(LanguageTag.UNDETERMINED_PREFIX)
            else -> tag
        },
    )
}

public actual val Locale.Companion.current: Locale
    get() = (NSLocale.preferredLanguages.first() as String).toLocale()

public actual fun Locale.Companion.setCurrent(locale: Locale?): Unit =
    if (locale == null) NSUserDefaults.standardUserDefaults.removeObjectForKey(LANG_KEY)
    else NSUserDefaults.standardUserDefaults.setObject(arrayListOf(locale.toString()), LANG_KEY)

