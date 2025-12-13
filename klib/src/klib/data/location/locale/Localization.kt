package klib.data.location.locale

public data class Localization(
    val locales: List<Locale> = emptyList(),
    val locale: Locale = Locale.current,
    val translations: Map<String, List<String>> = emptyMap()
) {

    public fun getStringOrNull(key: String, quantity: Int = 0): String? =
        translations[key]?.let { translation ->
            requireNotNull(translation.getOrNull(quantity)) {
                "No localization quantity '$quantity' in '$translation'"
            }
        }

    public fun getString(key: String, quantity: Int = 0): String =
        getStringOrNull(key, quantity) ?: run {
            localeLog.w(tag = Localization::class.simpleName!!) { "No localization key '$key'" }
            key
        }

    public fun getStringArrayOrNull(key: String): List<String>? = translations[key]

    public fun getStringArray(key: String): List<String>? = requireNotNull(getStringArrayOrNull(key)) {
        "No localization key '$key'"
    }
}
