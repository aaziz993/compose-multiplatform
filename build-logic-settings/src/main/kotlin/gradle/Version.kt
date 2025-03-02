package gradle

import io.github.z4kn4fein.semver.Version
import org.gradle.api.Project

// Semantic Versioning 2.0.0
// major gradle.version when you make incompatible API changes
// minor gradle.version when you add functionality in a backward compatible manner
// patch gradle.version when you make backward compatible bug fixes
// additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.
// a pre-release gradle.version MAY be denoted by appending a hyphen and a series of dot separated identifiers immediately following the patch gradle.version. Identifiers MUST comprise only ASCII alphanumerics and hyphens [0-9A-Za-z-]. Identifiers MUST NOT be empty. Numeric identifiers MUST NOT include leading zeroes. Examples: 1.0.0-alpha, 1.0.0-alpha.1, 1.0.0-0.3.7, 1.0.0-x.7.z.92, 1.0.0-x-y-z.--.
// build metadata MAY be denoted by appending a plus sign and a series of dot separated identifiers immediately following the patch or pre-release gradle.version. Identifiers MUST comprise only ASCII alphanumerics and hyphens [0-9A-Za-z-]. Identifiers MUST NOT be empty. Examples: 1.0.0-alpha+001, 1.0.0+20130313144700, 1.0.0-beta+exp.sha.5114f85, 1.0.0+21AF26D3----117B344092BD.
internal fun Project.version(): String =
    Version(
        settings.libs.versions.version("$moduleName.version.major")?.toInt() ?: projectProperties.version.major,
        settings.libs.versions.version("$moduleName.version.minor")?.toInt() ?: projectProperties.version.minor,
        settings.libs.versions.version("$moduleName.version.patch")?.toInt() ?: projectProperties.version.patch,
        settings.libs.versions.version("$moduleName.version.preRelease") ?: projectProperties.version.preRelease,
        "${
            gitRef?.takeIf{projectProperties.version.gitRef}.orEmpty()
        }${
            gitRunNumber?.takeIf{projectProperties.version.gitRunNumber}.orEmpty()
        }${
            spaceGitBranch?.takeIf{projectProperties.version.spaceGitBranch}.orEmpty()
        }${
            spaceExecutionNumber?.takeIf{projectProperties.version.spaceExecutionNumber}.orEmpty()
        }${
//            teamCityGitBranch?.takeIf{projectProperties.version.teamCityGitBranch}.orEmpty()
            ""
        }${
//            teamCityBuildNumber?.takeIf{projectProperties.version.teamCityBuildNumber}.orEmpty()
            ""
        }".ifEmpty { null },
    ).toString()

