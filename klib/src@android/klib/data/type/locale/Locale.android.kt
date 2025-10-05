//package klib.data.type.locale
//
//import androidx.core.os.LocaleListCompat
//import io.fluidsonic.locale.Locale
//
//public actual val CURRENT_LOCALE: Locale
//    get() = LocaleListCompat.getDefault()[0]!!.let {
//        Locale.forLanguage(it.language, it.script, it.country)
//    }
