package plugin.project.model

import gradle.version
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * This class describes a semantic version and related operations following the semver 2.0.0 specification.
 * Instances of this class are immutable, which makes them thread-safe.
 *
 * @sample io.github.z4kn4fein.semver.samples.VersionSamples.explode
 */
@Serializable
internal data class Version(
    val major: Int = 1,
    /** The MINOR number of the version. */
    val minor: Int = 0,
    /** The PATCH number of the version. */
    val patch: Int = 0,
    val parsedPreRelease: String? = null,
) {

    context(Project)
    fun toSemVer() = project.version(major, minor, patch, parsedPreRelease)
}
