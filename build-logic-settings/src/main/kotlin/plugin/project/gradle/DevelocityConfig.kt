package plugin.project.gradle

import com.gradle.develocity.agent.gradle.DevelocityConfiguration
import gradle.isCI
import gradle.versionCatalog
import java.io.File
import java.util.Date
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.execute
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.extension
import java.net.URLEncoder

internal fun Settings.configureDevelocity() {
    val versionCatalog = versionCatalog

    // Gives the data to speed up your build, improve build reliability and accelerate build debugging.
    apply(plugin = versionCatalog.getTable("plugins")!!.getTable("develocity")!!.getString("id")!!)
    // Enhances published build scans by adding a set of tags, links and custom values that have proven to be useful for many projects building with Develocity.
    apply(plugin = versionCatalog.getTable("plugins")!!.getTable("develocityCommonCustomUserData")!!.getString("id")!!)


    extensions.configure<DevelocityConfiguration>(::configureDevelocityConfiguration)

    enrichTeamCityData()
    enrichGitData()
}

private fun Settings.configureDevelocityConfiguration(configuration: DevelocityConfiguration) = configuration.apply {
    with(settings.extension) {
        val startParameter = gradle.startParameter
        val scanJournal = File(settingsDir, "scan-journal.log")

        server = providers.gradleProperty("develocity.server").get()

        buildScan {
            uploadInBackground = !isCI

            // obfuscate NIC since we don't want to expose user real IP (will be relevant without VPN)
            obfuscation {
                ipAddresses { addresses -> addresses.map { _ -> "0.0.0.0" } }
            }

            capture {
                fileFingerprints = true
            }

            buildScanPublished {
                scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
            }

            val skipBuildScans = providers.gradleProperty("develocity.skip-build-scans")
                .getOrElse("false")
                .toBooleanStrict()

            publishing.onlyIf { it.isAuthenticated && !skipBuildScans }
        }

        buildCache {

            if (isCI) {
                local {
                    isEnabled = providers.gradleProperty("develocity.build.cache.local.enable").get().toBoolean()
                }
            }

            remote(buildCache) {
                isEnabled = providers.gradleProperty("develocity.build.cache.remote.enable").get().toBoolean()
                // Check access key presence to avoid build cache errors on PR builds when access key is not present
                val accessKey = System.getenv().getOrElse("GRADLE_ENTERPRISE_ACCESS_KEY") {
                    localProperties.getProperty("gradle.enterprise.access-key")
                }
                isPush = isCI && accessKey != null
            }
        }
    }
}

private fun Settings.enrichTeamCityData() {
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

private fun Settings.enrichGitData() {
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



