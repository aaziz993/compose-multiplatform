package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DiktatConfig(
    val version: String? = null,
    val config: String? = null
)
