package processor.generators.location.currency.model

import kotlinx.serialization.Serializable

@Serializable
public data class Currency(
    val name: String? = null,
    val demonym: String,
    val majorSingle: String? = null,
    val majorPlural: String? = null,
    val ISOnum: Int? = null,
    val symbol: String? = null,
    val symbolNative: String? = null,
    val minorSingle: String? = null,
    val minorPlural: String? = null,
    val ISOdigits: Int,
    val decimals: Int? = null,
    val numToBasic: Int? = null,
)
