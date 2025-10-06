package processor.generators.location.locale.model

import kotlinx.serialization.Serializable

@Serializable
public data class LanguageTag(
    public val extensions: Set<String> = emptySet(),
    public val extlangs: Set<String> = emptySet(),
    public val language: String? = null,
    public val privateUse: String? = null,
    public val region: String? = null,
    public val script: String? = null,
    public val variants: Set<String> = emptySet(),
)
