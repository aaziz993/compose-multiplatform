package plugin.project.gradle.develocity

import gradle.api.gitBranchName
import gradle.api.gitCommitId
import gradle.api.gitStatus
import gradle.accessors.id
import gradle.api.CI
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.api.teamCityBuildId
import gradle.api.teamCityBuildTypeId
import java.net.URLEncoder
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity
import plugin.project.gradle.develocity.model.DevelocitySettings

internal class DevelocityPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.plugins.develocity.takeIf(DevelocitySettings::enabled)?.let { develocity ->
                // Gives the data to speed up your build, improve build reliability and accelerate build debugging.
                plugins.apply(settings.libs.plugins.plugin("develocity").id)

                // Enhances published build scans by adding a set of tags, links and custom values that have proven to be useful for many projects building with Develocity.
                plugins.apply(settings.libs.plugins.plugin("develocityCommonCustomUserData").id)

                develocity.applyTo()

                enrichTeamCityData()
                enrichGitData()
            }
        }
    }

    private fun Settings.enrichTeamCityData() {
        val teamCityUrl = providers.gradleProperty("team-city.url").orNull

        if (teamCityUrl == null) {
            return
        }

        gradle.projectsEvaluated {
            if (CI) {
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

    private fun Settings.enrichGitData() = projectProperties.plugins.develocity.let { develocity ->
        develocity.git?.let { git ->
            gradle.projectsEvaluated {
                if (!CI && !git.skipTags) {
                    develocity {
                        git.repo.let { repo ->
                            // Git commit id
                            val commitId = gitCommitId()
                            if (commitId.isNotEmpty()) {
                                buildScan.value("Git Commit ID", commitId)
                                buildScan.link("GitHub Commit Link", "$repo/tree/$commitId")
                            }

                            // Git branch name
                            val branchName = gitBranchName()
                            if (branchName.isNotEmpty()) {
                                buildScan.value("Git Branch Name", branchName)
                                buildScan.link("GitHub Branch Link", "$repo/tree/$branchName")
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
    }
}
