package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class VersionSettings(
    val major: Int = 1,
    val minor: Int = 0,
    val patch: Int = 0,
    val preRelease: String? = null,
    val gitRef: Boolean = true,
    val gitRunNumber: Boolean = true,
    val spaceGitBranch: Boolean = true,
    val spaceExecutionNumber: Boolean = true,
    val teamCityGitBranch: Boolean = true,
    val teamCityBuildNumber: Boolean = true,
)
