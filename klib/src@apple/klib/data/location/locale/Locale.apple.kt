package klib.data.location.locale

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.regionCode
import platform.Foundation.scriptCode

public actual fun currentLocale(): Locale = NSLocale.currentLocale.let { locale ->
    Locale.forLanguage(locale.languageCode, locale.scriptCode, locale.regionCode)
}
