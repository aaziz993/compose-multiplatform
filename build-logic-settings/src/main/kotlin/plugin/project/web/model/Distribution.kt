package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Distribution(
    val distributionName: String? = null,
    val outputDirectory: String? = null,
)
