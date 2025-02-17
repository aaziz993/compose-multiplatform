package plugin.project.gradle.develocity

import com.gradle.develocity.agent.gradle.DevelocityConfiguration
import gradle.*
import org.gradle.api.initialization.Settings
import plugin.project.gradle.develocity.model.DevelocitySettings
import java.net.URLEncoder

internal class DevelocityPluginPart(private val settings: Settings) {

    private val develocity by lazy {
        DevelocitySettings()
    }

    val needToApply: Boolean by lazy {
        develocity.enabled
    }

    fun apply() {
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

        val ge = extensions.getByType(DevelocityConfiguration::class.java)

        gradle.projectsEvaluated {
            if (isCI) {
                val buildTypeId = "teamcity.buildType.id"
                val buildId = "teamcity.build.id"

                if (gradle.rootProject.hasProperty(buildId) && gradle.rootProject.hasProperty(buildTypeId)) {
                    val buildIdValue = gradle.rootProject.property(buildId).toString()
                    val teamCityBuildNumber = URLEncoder.encode(buildIdValue, "UTF-8")
                    val teamCityBuildTypeId = gradle.rootProject.property(buildTypeId)

                    ge.buildScan.link(
                        "${rootProject.name.uppercase()} TeamCity build",
                        "$teamCityUrl/buildConfiguration/${teamCityBuildTypeId}/${teamCityBuildNumber}",
                    )
                }

                if (gradle.rootProject.hasProperty(buildId)) {
                    ge.buildScan.value("TeamCity CI build id", gradle.rootProject.property(buildId) as String)
                }
            }
        }
    }

    private fun Settings.enrichGitData() = with(settings) {
        val ge = extensions.getByType(DevelocityConfiguration::class.java)

        val githubRepo = providers.gradleProperty("develocity.github.repo").get()
        val skipGitTags = providers.gradleProperty("develocity.github.skip-git-tags").map(String::toBoolean)
            .getOrElse(false)

        gradle.projectsEvaluated {
            if (!isCI && !skipGitTags) {
                // Git commit id
                val commitId = execute("git rev-parse --verify HEAD")
                if (commitId.isNotEmpty()) {
                    ge.buildScan.value("Git Commit ID", commitId)
                    ge.buildScan.link("GitHub Commit Link", "$githubRepo/tree/$commitId")
                }

                // Git branch name
                val branchName = execute("git rev-parse --abbrev-ref HEAD")
                if (branchName.isNotEmpty()) {
                    ge.buildScan.value("Git Branch Name", branchName)
                    ge.buildScan.link("GitHub Branch Link", "$githubRepo/tree/$branchName")
                }

                // Git dirty local state
                val status = execute("git status --porcelain")
                if (status.isNotEmpty()) {
                    ge.buildScan.value("Git Status", status)
                }
            }
        }
    }
}
