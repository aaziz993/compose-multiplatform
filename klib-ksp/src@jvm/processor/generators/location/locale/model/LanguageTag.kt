package processor.generators.location.locale.model

import kotlinx.serialization.Serializable

@Serializable
public data class LanguageTag(
    val lang: String,
    val langType: String,
    val territory: String? = null,
)
