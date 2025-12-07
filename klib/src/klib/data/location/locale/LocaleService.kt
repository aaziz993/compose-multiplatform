package klib.data.location.locale

import com.diamondedge.logging.logging
import klib.data.type.primitives.string.format

public abstract class LocaleService {

    public open lateinit var locale: Locale
        protected set

    public open lateinit var translations: Map<String, List<String>>
        protected set

    public abstract suspend fun getLocales(): List<Locale>

    public open suspend fun setLocale(locale: Locale) {
        this.locale = locale
    }

    public fun localize(key: String, quantity: Int = 0, vararg formatArgs: Any): String? =
        translations[key]?.let { translation ->
            requireNotNull(translation.getOrNull(quantity)) {
                "No localization quantity '$quantity' in '$translation'"
            }.format(*formatArgs)
        } ?: run {
            log.w { "No localization key '$key' for locale '$locale'" }
            key
        }

    public companion object Companion {

        private val log = logging()
    }
}
