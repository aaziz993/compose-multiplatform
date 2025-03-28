package gradle.plugins.project.ci

import kotlinx.serialization.Serializable

@Serializable
internal data class GitHubActions(
    override val formatCheck: Boolean = true,
    override val test: Boolean = true,
    override val qualityCheck: Boolean = true,
    override val publishToGithubPackages: Boolean = true,
    override val publishToSpacePackages: Boolean = false,
    override val publishToMavenRepository: Boolean = false,
) : CI
