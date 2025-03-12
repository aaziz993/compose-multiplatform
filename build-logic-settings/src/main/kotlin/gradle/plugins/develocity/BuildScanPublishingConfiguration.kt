package gradle.plugins.develocity

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanPublishingConfiguration(
    val ifAuthenticated: Boolean? = null
)
