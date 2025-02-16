package plugin.project.web.npm.model

import kotlinx.serialization.Serializable

@Serializable
internal data class NpmOverride(
    val path: String,
    val includedVersions: List<String>? = null,
    val excludedVersions: List<String>? = null,
)
