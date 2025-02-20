package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinWebpackOutput(
    val library: String? = null,
    val libraryTarget: String? = null,
    val globalObject: String? = null
)
