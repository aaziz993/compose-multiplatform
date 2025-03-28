package gradle.plugins.project.ci

import kotlinx.serialization.Serializable

@Serializable
internal data class TeamCityAutomation(
    override val test: Boolean = true,
    override val formatCheck: Boolean = true,
    override val qualityCheck: Boolean = true,
    override val publishToGithubPackages: Boolean = false,
    override val publishToSpacePackages: Boolean = true,
    override val publishToMavenRepository: Boolean = true,
) : CI
