package plugin.project.gradle.develocity.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanPublishingConfiguration(
    val onlyIfAuthenticated: Boolean? = null
)
