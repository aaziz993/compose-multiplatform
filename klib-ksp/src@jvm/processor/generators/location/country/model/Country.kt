package processor.generators.location.country.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Country(
    val name: String,
    @SerialName("alpha-2")
    val alpha2: String,
    @SerialName("alpha-3")
    val alpha3: String,
    @SerialName("country-code")
    val countryCode: String,
    @SerialName("iso_3166-2")
    val iso31662: String? = null,
    val region: String? = null,
    @SerialName("sub-region")
    val subRegion: String? = null,
    @SerialName("intermediate-region")
    val intermediateRegion: String? = null,
    @SerialName("region-code")
    val regionCode: String? = null,
    @SerialName("sub-region-code")
    val subRegionCode: String? = null,
    @SerialName("intermediate-region-code")
    val intermediateRegionCode: String? = null,
)
