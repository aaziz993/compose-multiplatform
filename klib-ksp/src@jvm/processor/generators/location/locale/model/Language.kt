package processor.generators.location.locale.model;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Language(
    @SerialName("alpha3-b")
    val alpha3b: String,
    @SerialName("alpha3-t")
    val alpha3t: String? = null,
    val alpha2: String? = null,
    @SerialName("English")
    public val name: String,
)
