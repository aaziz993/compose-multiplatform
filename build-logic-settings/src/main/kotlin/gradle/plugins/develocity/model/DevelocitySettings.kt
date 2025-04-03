package gradle.plugins.develocity.model

import gradle.api.project.projectProperties
import gradle.api.ci.CI
import klib.data.type.primitive.isGithubUrl
import gradle.plugins.develocity.DevelocityConfiguration
import gradle.plugins.develocity.buildscan.BuildScanConfiguration
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
    val gitRepo: String? = null,
    val skipGitTags: Boolean = false,
    val teamCityUrl: String? = null,
) : DevelocityConfiguration {

    context(Settings)
    override fun applyTo() =
        settings.pluginManager.withPlugin("com.gradle.develocity") {
            super.applyTo()

            settings.enrichGitData()
            settings.enrichTeamCityData()
        }

    private fun Settings.enrichGitData() = projectProperties.develocity?.let { _develocity ->
        gradle.projectsEvaluated {
            if (!CI.present && !_develocity.skipGitTags) {
                (_develocity.gitRepo ?: projectProperties.remote?.url?.takeIf(String::isGithubUrl))?.let { gitRepo ->
                    // Git commit id
                    val commitId = CI.Github.commitId
                    if (commitId.isNotEmpty()) {
                        develocity.buildScan.value("Git Commit ID", commitId)
                        develocity.buildScan.link("GitHub Commit Link", "$gitRepo/tree/$commitId")
                    }

                    // Git branch name
                    val branchName = CI.Github.ref.orEmpty()
                    if (branchName.isNotEmpty()) {
                        develocity.buildScan.value("Git Branch Name", branchName)
                        develocity.buildScan.link("GitHub Branch Link", "$gitRepo/tree/$branchName")
                    }
                }

                // Git dirty local state
                val status = CI.Github.status
                if (status.isNotEmpty()) {
                    develocity.buildScan.value("Git Status", status)
                }
            }
        }
    }

    private fun Settings.enrichTeamCityData() {
        if (teamCityUrl == null) {
            return
        }

        gradle.projectsEvaluated {
            with(rootProject) {
                if (CI.present) {
                    CI.TeamCity.buildId?.let { teamCityBuildId ->
                        CI.TeamCity.buildTypeId?.let { teamCityBuildTypeId ->
                            val teamCityBuildNumber = URLEncoder.encode(teamCityBuildId, "UTF-8")

                            develocity.buildScan.link(
                                "${rootProject.name.uppercase()} TeamCity build",
                                "$teamCityUrl/buildConfiguration/${teamCityBuildTypeId}/${teamCityBuildNumber}",
                            )
                        }

                        develocity.buildScan.value("TeamCity CI build id", teamCityBuildId)
                    }
                }
            }
        }
    }
}
