package klib.data.location.locale

import com.diamondedge.logging.logging
import klib.data.type.primitives.string.format

private val log = logging("LocaleService")

public open class LocaleService {

    public open suspend fun getLocales(): List<Locale> = emptyList()

    public open suspend fun getTranslations(locale: Locale): Map<String, List<String>> = emptyMap()
}

public fun Map<String, List<String>>.getStringOrNull(key: String, quantity: Int = 0, vararg formatArgs: Any): String? =
    this[key]?.let { translation ->
        requireNotNull(translation.getOrNull(quantity)) {
            "No localization quantity '$quantity' in '$translation'"
        }.format(*formatArgs)
    }

public fun Map<String, List<String>>.getString(key: String, quantity: Int = 0, vararg formatArgs: Any): String =
    getStringOrNull(key, quantity, *formatArgs) ?: run {
        log.w { "No localization key '$key'" }
        key
    }

