package plugins.develocity.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.api.isCI
import gradle.api.gitBranchName
import gradle.api.gitCommitId
import gradle.api.gitStatus
import gradle.api.teamCityBuildId
import gradle.api.teamCityBuildTypeId
import gradle.isGithubUrl
import gradle.plugins.develocity.BuildScanConfiguration
import gradle.plugins.develocity.DevelocityConfiguration
import gradle.project.EnabledSettings
import java.net.URLEncoder
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity

@Serializable
internal data class DevelocitySettings(
    override val buildScan: BuildScanConfiguration? = null,
    override val server: String? = null,
    override val edgeDiscovery: Boolean? = null,
    override val projectId: String? = null,
    override val allowUntrustedServer: Boolean? = null,
    override val accessKey: String? = null,
    override val enabled: Boolean = true,
    val gitRepo: String? = null,
    val skipGitTags: Boolean = false,
    val teamCityUrl: String? = null,
) : DevelocityConfiguration, EnabledSettings {

    context(Settings)
    override fun applyTo() =
        pluginManager.withPlugin(libs.plugins.plugin("develocity").id) {
            super.applyTo()

            enrichGitData()
            enrichTeamCityData()
        }

    private fun Settings.enrichGitData() = projectProperties.plugins.develocity.let { develocity ->
        gradle.projectsEvaluated {
            if (!isCI && !develocity.skipGitTags) {
                develocity {
                    (develocity.gitRepo ?: projectProperties.remote?.url?.takeIf(String::isGithubUrl))?.let { gitRepo ->
                        // Git commit id
                        val commitId = gitCommitId()
                        if (commitId.isNotEmpty()) {
                            buildScan.value("Git Commit ID", commitId)
                            buildScan.link("GitHub Commit Link", "$gitRepo/tree/$commitId")
                        }

                        // Git branch name
                        val branchName = gitBranchName()
                        if (branchName.isNotEmpty()) {
                            buildScan.value("Git Branch Name", branchName)
                            buildScan.link("GitHub Branch Link", "$gitRepo/tree/$branchName")
                        }
                    }

                    // Git dirty local state
                    val status = gitStatus()
                    if (status.isNotEmpty()) {
                        buildScan.value("Git Status", status)
                    }
                }
            }
        }
    }

    private fun Settings.enrichTeamCityData() {
        if (teamCityUrl == null) {
            return
        }

        gradle.projectsEvaluated {
            if (isCI) {
                develocity {
                    rootProject.teamCityBuildId?.let { teamCityBuildId ->
                        rootProject.teamCityBuildTypeId?.let { teamCityBuildTypeId ->
                            val teamCityBuildNumber = URLEncoder.encode(teamCityBuildId, "UTF-8")

                            buildScan.link(
                                "${rootProject.name.uppercase()} TeamCity build",
                                "$teamCityUrl/buildConfiguration/${teamCityBuildTypeId}/${teamCityBuildNumber}",
                            )
                        }

                        buildScan.value("TeamCity CI build id", teamCityBuildId)
                    }
                }
            }
        }
    }
}
