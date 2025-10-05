//package klib.data.type
//
//import java.util.Locale
//
//public actual val CURRENT_LOCALE: io.fluidsonic.locale.Locale
//    get() = Locale.getDefault().let {
//        io.fluidsonic.locale.Locale.forLanguage(
//            it.language,
//            it.script,
//            it.country,
//        )
//    }
