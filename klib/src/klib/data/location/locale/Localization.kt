package klib.data.location.locale

import com.diamondedge.logging.logging
import klib.data.type.primitives.string.format

public data class Localization(
    val locales: List<Locale> = emptyList(),
    val locale: Locale = Locale.current,
    val translations: Map<String, List<String>> = emptyMap()
) {

    public fun getStringOrNull(key: String, quantity: Int = 0, vararg formatArgs: Any): String? =
        translations[key]?.let { translation ->
            requireNotNull(translation.getOrNull(quantity)) {
                "No localization quantity '$quantity' in '$translation'"
            }.format(*formatArgs)
        }

    public fun getString(key: String, quantity: Int = 0, vararg formatArgs: Any): String =
        getStringOrNull(key, quantity, *formatArgs) ?: run {
            log.w { "No localization key '$key'" }
            key
        }

    public fun getStringArrayOrNull(key: String): List<String>? = translations[key]

    public fun getStringArray(key: String): List<String>? = requireNotNull(getStringArrayOrNull(key)) {
        "No localization key '$key'"
    }

    public companion object {

        private val log = logging("LocaleService")
    }
}
