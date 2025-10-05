package klib.data.location.locale

public class Locale private constructor(
    private val languageTag: LanguageTag,
) {

    override fun equals(other: Any?): Boolean =
        this === other || (other is Locale && languageTag == other.languageTag)

    override fun hashCode(): Int =
        languageTag.hashCode()

    public val language: String?
        get() = when (val language = languageTag.language) {
            LanguageTag.Companion.undeterminedPrefix -> null
            else -> language
        }

    public val region: String?
        get() = languageTag.region

    public val script: String?
        get() = languageTag.script

    public val variants: List<String>
        get() = languageTag.variants

    public fun toLanguageTag(): LanguageTag =
        languageTag

    override fun toString(): String =
        languageTag.toString()

    public companion object {

        public val root: Locale = Locale(languageTag = LanguageTag.Companion.forLanguage(language = LanguageTag.Companion.undeterminedPrefix))

        public fun forLanguage(
            language: String?,
            script: String? = null,
            region: String? = null,
            variants: List<String> = emptyList(),
        ): Locale =
            forLanguageTag(
                    LanguageTag.Companion.forLanguage(
                            language = when (language) {
                                null, "" -> LanguageTag.Companion.undeterminedPrefix
                                else -> language
                            },
                            script = script,
                            region = region,
                            variants = variants,
                    ),
            )

        public fun forLanguageOrNull(
            language: String?,
            script: String? = null,
            region: String? = null,
            variants: List<String> = emptyList(),
        ): Locale? =
            LanguageTag.Companion.forLanguageOrNull(
                language = when (language) {
                    null, "" -> LanguageTag.Companion.undeterminedPrefix
                    else -> language
                },
                script = script,
                region = region,
                variants = variants,
            )?.let(::forLanguageTag)

        public fun forLanguageTag(tag: LanguageTag): Locale =
            when (tag) {
                root.languageTag -> root
                else -> Locale(languageTag = tag)
            }

        public fun forLanguageTag(tag: String): Locale =
            forLanguageTag(LanguageTag.Companion.parse(tag))

        public fun forLanguageTagOrNull(tag: String): Locale? =
            LanguageTag.Companion.parseOrNull(tag)?.let(::forLanguageTag)
    }
}

public expect val Locale.Companion.current: Locale

public fun String.toLocale(delimiter: String = "-"): Locale =
    Locale.forLanguage(substringBefore(delimiter), region = substringAfter(delimiter, "").ifEmpty { null })

public fun String.toLocaleOrNull(delimiter: String = "-"): Locale? =
    Locale.forLanguageOrNull(substringBefore(delimiter), region = substringAfter(delimiter, ""))

