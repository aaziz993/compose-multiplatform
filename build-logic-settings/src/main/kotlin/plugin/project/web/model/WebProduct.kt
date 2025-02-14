package plugin.project.web.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class WebProduct(
    @SerialName("web-app")
    val webApp: Boolean=false,
)
