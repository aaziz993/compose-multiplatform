package gradle.plugins.project

import kotlinx.serialization.Serializable

@Serializable
internal data class Plugins(
    val ids: Set<String>? = null,
)
