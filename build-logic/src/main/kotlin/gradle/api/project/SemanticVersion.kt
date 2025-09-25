package gradle.api.project

import gradle.api.ci.CI
import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
public data class SemanticVersion(
    val major: Int? = null,
    val minor: Int? = null,
    val patch: Int? = null,
    val preRelease: String? = null,
    val buildMetadata: String? = null,
) {

    // Semantic Versioning 2.0.0
    // major gradle.api when you make incompatible API changes
    // minor gradle.api when you add functionality in a backward compatible manner
    // patch gradle.api when you make backward compatible bug fixes
    // additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.
    // a pre-release gradle.api MAY be denoted by appending a hyphen and a series of dot separated identifiers immediately following the patch gradle.api. Identifiers MUST comprise only ASCII alphanumerics and hyphens [0-9A-Za-z-]. Identifiers MUST NOT be empty. Numeric identifiers MUST NOT include leading zeroes. Examples: 1.0.0-alpha, 1.0.0-alpha.1, 1.0.0-0.3.7, 1.0.0-x.7.z.92, 1.0.0-x-y-z.--.
    // build metadata MAY be denoted by appending a plus sign and a series of dot separated identifiers immediately following the patch or pre-release gradle.api. Identifiers MUST comprise only ASCII alphanumerics and hyphens [0-9A-Za-z-]. Identifiers MUST NOT be empty. Examples: 1.0.0-alpha+001, 1.0.0+20130313144700, 1.0.0-beta+exp.sha.5114f85, 1.0.0+21AF26D3----117B344092BD.
    context(project: Project)
    public fun toVersion(): Version {
        val projectDotName = project.name.replace("-", ".")

        return Version(
            major ?: project.libs.versions("$projectDotName.major").requiredVersion.toInt(),
            minor ?: project.libs.versions("$projectDotName.minor").requiredVersion.toInt(),
            patch ?: project.libs.versions("$projectDotName.patch").requiredVersion.toInt(),
            preRelease
                ?: project.libs.versions["$projectDotName.preRelease"]?.requiredVersion,
            buildMetadata
                ?: project.libs.versions["$projectDotName.buildMetadata"]?.requiredVersion
                ?: CI?.buildMetadata,
        )
    }
}
