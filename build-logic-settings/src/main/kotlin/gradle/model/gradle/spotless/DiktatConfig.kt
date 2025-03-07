package gradle.model.gradle.spotless

import kotlinx.serialization.Serializable

@Serializable
internal data class DiktatConfig(
    val version: String? = null,
    val config: String? = null
)
