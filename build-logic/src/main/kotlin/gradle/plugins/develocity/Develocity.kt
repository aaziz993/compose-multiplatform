package gradle.plugins.develocity

import com.gradle.develocity.agent.gradle.DevelocityConfiguration
import gradle.api.ci.CI
import gradle.api.ci.CI.TeamCity.Companion.gitBranch
import gradle.api.initialization.settingsProperties
import java.net.URLEncoder
import klib.data.type.isGithubUrl
import org.gradle.api.initialization.Settings
import gradle.api.initialization.gitCommitId
import gradle.api.initialization.gitBranch
import gradle.api.initialization.gitStatus

context(settings: Settings)
public fun DevelocityConfiguration.enrichGitData(
    url: String,
    skipTags: Boolean = false
): Unit = settings.pluginManager.withPlugin("com.gradle.develocity") {
    settings.gradle.projectsEvaluated {
        if (CI.current == null && !skipTags) {
            // Git commit id
            settings.gitCommitId()
                .takeIf(String::isNotBlank)
                ?.let { commitId ->
                    buildScan.value("Git Commit ID", commitId)
                    buildScan.link("GitHub Commit", "$url/commit/$commitId")
                }

            // Git branch name
            settings.gitBranch()
                ?.takeIf(String::isNotBlank)
                ?.let { branchName ->
                    buildScan.value("Git Branch Name", branchName)
                    buildScan.link("GitHub Branch Link", "$url/tree/$branchName")
                }
        }

        // Git dirty local state
        settings.gitStatus()
            .takeIf(String::isNotBlank)
            ?.let { status -> buildScan.value("Git Status", status) }
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
