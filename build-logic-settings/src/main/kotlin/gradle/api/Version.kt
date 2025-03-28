package gradle.api

import gradle.accessors.catalog.libs
import gradle.accessors.moduleName
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import io.github.z4kn4fein.semver.Version
import net.pearx.kasechange.toCamelCase
import org.gradle.api.Project

// Semantic Versioning 2.0.0
// major gradle.api.version when you make incompatible API changes
// minor gradle.api.version when you add functionality in a backward compatible manner
// patch gradle.api.version when you make backward compatible bug fixes
// additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.
// a pre-release gradle.api.version MAY be denoted by appending a hyphen and a series of dot separated identifiers immediately following the patch gradle.api.version. Identifiers MUST comprise only ASCII alphanumerics and hyphens [0-9A-Za-z-]. Identifiers MUST NOT be empty. Numeric identifiers MUST NOT include leading zeroes. Examples: 1.0.0-alpha, 1.0.0-alpha.1, 1.0.0-0.3.7, 1.0.0-x.7.z.92, 1.0.0-x-y-z.--.
// build metadata MAY be denoted by appending a plus sign and a series of dot separated identifiers immediately following the patch or pre-release gradle.api.version. Identifiers MUST comprise only ASCII alphanumerics and hyphens [0-9A-Za-z-]. Identifiers MUST NOT be empty. Examples: 1.0.0-alpha+001, 1.0.0+20130313144700, 1.0.0-beta+exp.sha.5114f85, 1.0.0+21AF26D3----117B344092BD.
internal fun Project.version(): String {
    val camelCaseName = project.name.toCamelCase()
    return Version(
        project.settings.libs.version("$camelCaseName.version.major")?.toInt()
            ?: projectProperties.version.major,
        project.settings.libs.version("$camelCaseName.version.minor")?.toInt()
            ?: projectProperties.version.minor,
        project.settings.libs.version("$camelCaseName.version.patch")?.toInt()
            ?: projectProperties.version.patch,
        project.settings.libs.version("$camelCaseName.version.preRelease")
            ?: projectProperties.version.preRelease,
        project.settings.libs.version("$camelCaseName.version.buildMeta") ?: "${
            gitRef?.takeIf { projectProperties.version.gitRef }.orEmpty()
        }${
            gitRunNumber?.takeIf { projectProperties.version.gitRunNumber }.orEmpty()
        }${
            spaceGitBranch?.takeIf { projectProperties.version.spaceGitBranch }.orEmpty()
        }${
            spaceExecutionNumber?.takeIf { projectProperties.version.spaceExecutionNumber }.orEmpty()
        }${
//            teamCityGitBranch?.takeIf{projectProperties.version.teamCityGitBranch}.orEmpty()
            ""
        }${
//            teamCityBuildNumber?.takeIf{projectProperties.version.teamCityBuildNumber}.orEmpty()
            ""
        }".ifEmpty { null },
    ).toString()
}

internal fun String.toVersion() = Version.parse(this)
