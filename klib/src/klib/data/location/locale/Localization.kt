package klib.data.location.locale

import com.diamondedge.logging.logging
import klib.data.type.primitives.string.format

public class Localization(
    public val locales: List<Locale>,
    public val locale: Locale,
    public val translations: Map<String, List<String>>,
) {

    public fun translate(key: String, quantity: Int = 0, vararg formatArgs: Any): String? =
        translations[key]?.let { translation ->
            requireNotNull(translation.getOrNull(quantity)) {
                "No translation for quantity '$quantity' in '$translation'"
            }.format(*formatArgs)
        } ?: run {
            log.w { "No translation key '$key' in locale '$locale'" }
            key
        }

    public companion object Companion {

        private val log = logging()
    }
}


