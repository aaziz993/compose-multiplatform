package plugin.project.kotlin.apollo.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinService(
    val sourceFolder: String,
    val nameSuffix: String,
    val service: Service? = null
)
