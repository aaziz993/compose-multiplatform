package gradle.api.repositories.maven

import gradle.api.repositories.Module
import gradle.api.repositories.RepositoryContentDescriptor
import gradle.api.repositories.Version
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.repositories.MavenRepositoryContentDescriptor

/**
 * Extends the repository content descriptor with Maven repositories specific options.
 *
 * @since 5.1
 */
@Serializable
internal data class MavenRepositoryContentDescriptor(
    override val excludeGroupsAndSubgroups: Set<String>? = null,
    override val excludeGroupByRegexes: Set<String>? = null,
    override val excludeModules: Set<Module>? = null,
    override val excludeModulesByRegexes: Set<Module>? = null,
    override val excludeVersions: Set<Version>? = null,
    override val excludeVersionsByRegexes: Set<Version>? = null,
    override val notForConfigurations: Set<String>? = null,
    override val includeGroups: Set<String>? = null,
    override val includeGroupsAndSubgroups: Set<String>? = null,
    override val includeGroupsByRegexes: Set<String>? = null,
    override val includeModules: Set<Module>? = null,
    override val includeModulesByRegexes: Set<Module>? = null,
    override val includeVersions: Set<Version>? = null,
    override val includeVersionsByRegexes: Set<Version>? = null,
    override val excludeGroups: Set<String>? = null,
    /**
     * Declares that this repository only contains releases.
     */
    val releasesOnly: Boolean? = null,
    /**
     * Declares that this repository only contains snapshots.
     */
    val snapshotsOnly: Boolean? = null,
) : RepositoryContentDescriptor<MavenRepositoryContentDescriptor> {

    override fun applyTo(receiver: MavenRepositoryContentDescriptor) {
        super.applyTo(receiver)

        receiver::releasesOnly trySet releasesOnly
        receiver::snapshotsOnly trySet snapshotsOnly
    }
}
