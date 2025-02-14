package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.web.model.WebProduct

@Serializable
internal data class Properties(
    val product: WebProduct,
    val apply: Set<String>? = null,
    val settings: Settings? = null,
)
