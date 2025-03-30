package gradle.plugins.project

import kotlinx.serialization.Serializable

@Serializable
internal data class VersionSettings(
    val major: Int = 1,
    val minor: Int = 0,
    val patch: Int = 0,
    val preRelease: String? = null,
    val buildMetadata: String? = null,
)
