//package klib.data.type.locale
//
//import io.fluidsonic.locale.Locale
//import platform.Foundation.NSLocale
//import platform.Foundation.currentLocale
//import platform.Foundation.languageCode
//import platform.Foundation.regionCode
//import platform.Foundation.scriptCode
//
//public actual val CURRENT_LOCALE: Locale
//    get() = NSLocale.currentLocale.let {
//        Locale.forLanguage(it.languageCode, it.scriptCode, it.regionCode)
//    }
