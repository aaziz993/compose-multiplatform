package plugin.project.kotlin.apollo.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AndroidService(
    val sourceFolder: String,
    val nameSuffix: String,
    val service: Service? = null
)
