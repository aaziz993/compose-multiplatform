package plugin.project.web.yarn

import kotlinx.serialization.Serializable

@Serializable
internal data class YarnResolution(
    val path: String,
    val includedVersions: List<String>? = null,
    val excludedVersions: List<String>? = null,
)
