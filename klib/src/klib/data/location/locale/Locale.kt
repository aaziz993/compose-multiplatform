package klib.data.location.locale

import klib.data.location.country.Country

public data class Locale(
    private val languageTag: LanguageTag,
) {

    public val language: String?
        get() = when (val language = languageTag.language) {
            LanguageTag.UNDETERMINED_PREFIX -> null
            else -> language
        }

    public val region: String?
        get() = languageTag.region

    public val script: String?
        get() = languageTag.script

    public val variants: List<String>
        get() = languageTag.variants

    public fun toLanguageTag(): LanguageTag = languageTag

    public fun country(): Country? =
        languageTag.region?.let(Country::forCodeOrNull) ?: language?.let(Country::forLanguageOrNull)

    override fun toString(): String = languageTag.toString()

    public companion object {

        public val root: Locale = Locale(LanguageTag.forLanguage(LanguageTag.UNDETERMINED_PREFIX))

        public fun forLanguage(
            language: String?,
            script: String? = null,
            region: String? = null,
            variants: List<String> = emptyList(),
        ): Locale = forLanguageTag(
            LanguageTag.forLanguage(
                when (language) {
                    null, "" -> LanguageTag.UNDETERMINED_PREFIX
                    else -> language
                },
                script,
                region,
                variants,
            ),
        )

        public fun forLanguageOrNull(
            language: String?,
            script: String? = null,
            region: String? = null,
            variants: List<String> = emptyList(),
        ): Locale? = LanguageTag.forLanguageOrNull(
            when (language) {
                null, "" -> LanguageTag.UNDETERMINED_PREFIX
                else -> language
            },
            script,
            region,
            variants,
        )?.let(::forLanguageTag)

        public fun forLanguageTag(tag: LanguageTag): Locale =
            when (tag) {
                root.languageTag -> root
                else -> Locale(languageTag = tag)
            }

        public fun forLanguageTag(tag: String): Locale = forLanguageTag(LanguageTag.parse(tag))

        public fun forLanguageTagOrNull(tag: String): Locale? =
            LanguageTag.parseOrNull(tag)?.let(::forLanguageTag)
    }
}

public expect val Locale.Companion.current: Locale

public expect fun Locale.Companion.setCurrent(locale: Locale?)

public fun String.toLocale(): Locale = Locale.forLanguageTag(this)

public fun String.toLocaleOrNull(): Locale? = Locale.forLanguageTagOrNull(this)

