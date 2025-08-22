package gradle.plugins.develocity

import com.gradle.develocity.agent.gradle.DevelocityConfiguration
import gradle.api.ci.CI
import gradle.api.initialization.settingsProperties
import java.net.URLEncoder
import klib.data.type.isGithubUrl
import org.gradle.api.initialization.Settings

context(settings: Settings)
public fun DevelocityConfiguration.enrichGitData(
    url: String,
    skipTags: Boolean = false
): Unit = settings.pluginManager.withPlugin("com.gradle.develocity") {
    settings.gradle.projectsEvaluated {
        if (CI.current == null && !skipTags) {
            // Git commit id
            val commitId = CI.Github.commitId
            if (commitId.isNotEmpty()) {
                buildScan.value("Git Commit ID", commitId)
                buildScan.link("GitHub Commit Link", "$url/tree/$commitId")
            }

            // Git branch name
            val branchName = CI.Github.ref.orEmpty()
            if (branchName.isNotEmpty()) {
                buildScan.value("Git Branch Name", branchName)
                buildScan.link("GitHub Branch Link", "$url/tree/$branchName")
            }
        }

        // Git dirty local state
        val status = CI.Github.status
        if (status.isNotEmpty()) {
            buildScan.value("Git Status", status)
        }
    }
}

context(settings: Settings)
public fun DevelocityConfiguration.enrichTeamCityData(url: String): Unit =
    settings.pluginManager.withPlugin("com.gradle.develocity") {
        settings.gradle.projectsEvaluated {
            with(rootProject) {
                if (CI.current != null) {
                    CI.TeamCity.buildId?.let { teamCityBuildId ->
                        CI.TeamCity.buildTypeId?.let { teamCityBuildTypeId ->
                            val teamCityBuildNumber = URLEncoder.encode(teamCityBuildId, "UTF-8")

                            buildScan.link(
                                "${rootProject.name.uppercase()} TeamCity build",
                                "$url/buildConfiguration/${teamCityBuildTypeId}/${teamCityBuildNumber}",
                            )
                        }

                        buildScan.value("TeamCity CI build id", teamCityBuildId)
                    }
                }
            }
        }
    }
