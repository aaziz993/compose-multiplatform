package plugin.project.gradle.develocity

import gradle.*
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity
import java.net.URLEncoder

internal class DevelocityPluginPart(private val settings: Settings) {

    private val develocity by lazy {
        settings.amperProjectExtraProperties.settings.gradle.develocity
    }

    val needToApply: Boolean by lazy { develocity.enabled }

    init {
        // Gives the data to speed up your build, improve build reliability and accelerate build debugging.
        settings.plugins.apply(settings.libs.plugins.plugin("develocity").id)

        // Enhances published build scans by adding a set of tags, links and custom values that have proven to be useful for many projects building with Develocity.
        settings.plugins.apply(settings.libs.plugins.plugin("develocityCommonCustomUserData").id)

        applySettings()
    }

    fun applySettings() = with(settings) {
        configureDevelocityConfiguration()
        enrichTeamCityData()
        enrichGitData()

    }


    private fun Settings.enrichTeamCityData() = with(settings) {
        val teamCityUrl = providers.gradleProperty("team-city.url").orNull

        if (teamCityUrl == null) {
            return
        }

        gradle.projectsEvaluated {
            if (isCI) {
                develocity {
                    val buildTypeId = "teamcity.buildType.id"
                    val buildId = "teamcity.build.id"

                    if (gradle.rootProject.hasProperty(buildId) && gradle.rootProject.hasProperty(buildTypeId)) {
                        val buildIdValue = gradle.rootProject.property(buildId).toString()
                        val teamCityBuildNumber = URLEncoder.encode(buildIdValue, "UTF-8")
                        val teamCityBuildTypeId = gradle.rootProject.property(buildTypeId)

                        buildScan.link(
                            "${rootProject.name.uppercase()} TeamCity build",
                            "$teamCityUrl/buildConfiguration/${teamCityBuildTypeId}/${teamCityBuildNumber}",
                        )
                    }

                    if (gradle.rootProject.hasProperty(buildId)) {
                        buildScan.value("TeamCity CI build id", gradle.rootProject.property(buildId) as String)
                    }
                }
            }
        }
    }

    private fun Settings.enrichGitData() = with(settings) {
        this@DevelocityPluginPart.develocity.git?.let { git ->
            gradle.projectsEvaluated {
                if (!isCI && !git.skipTags) {
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
