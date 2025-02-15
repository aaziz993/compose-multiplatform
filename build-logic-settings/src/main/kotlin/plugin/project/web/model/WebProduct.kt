package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class WebProduct(
    val webApp: Boolean = false,
)
