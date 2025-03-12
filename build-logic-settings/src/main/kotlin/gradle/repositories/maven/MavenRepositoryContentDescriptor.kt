package gradle.repositories.maven

import gradle.repositories.RepositoryContentDescriptor
import gradle.repositories.Version
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.repositories.InclusiveRepositoryContentDescriptor
import org.gradle.api.artifacts.repositories.MavenRepositoryContentDescriptor

/**
 * Extends the repository content descriptor with Maven repositories specific options.
 *
 * @since 5.1
 */
@Serializable
internal data class MavenRepositoryContentDescriptor(
    override val excludeGroupsAndSubgroups: List<String>? = null,
    override val excludeGroupByRegexes: List<String>? = null,
    override val excludeModules: List<Module>? = null,
    override val excludeModulesByRegexes: List<Module>? = null,
    override val excludeVersions: List<Version>? = null,
    override val excludeVersionsByRegexes: List<Version>? = null,
    override val notForConfigurations: List<String>? = null,
    override val includeGroups: List<String>? = null,
    override val includeGroupsAndSubgroups: List<String>? = null,
    override val includeGroupsByRegexes: List<String>? = null,
    override val includeModules: List<Module>? = null,
    override val includeModulesByRegexes: List<Module>? = null,
    override val includeVersions: List<Version>? = null,
    override val includeVersionsByRegexes: List<Version>? = null,
    /**
     * Declares that this repository only contains releases.
     */
    val releasesOnly: Boolean? = null,
    /**
     * Declares that this repository only contains snapshots.
     */
    val snapshotsOnly: Boolean? = null,
    override val excludeGroups: List<String>?,
) : RepositoryContentDescriptor {

    override fun applyTo(descriptor: InclusiveRepositoryContentDescriptor) {
        super.applyTo(descriptor)

        descriptor as MavenRepositoryContentDescriptor

        releasesOnly?.takeIf { it }?.run { descriptor.releasesOnly() }
        snapshotsOnly?.takeIf { it }?.run { descriptor.snapshotsOnly() }
    }
}
