package gradle.api.artifacts

import klib.data.type.reflection.trySet
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency

/**
 *
 * A `ExternalModuleDependency` is a [Dependency] on a module outside the current project hierarchy.
 */
internal data class ExternalModuleDependency(
    override val version: MutableVersionConstraint? = null,
    override val excludeRules: Set<ExcludeRule>? = null,
    override val artifacts: Set<DependencyArtifact>? = null,
    override val transitive: Boolean? = null,
    override val targetConfiguration: String? = null,
    override val capabilities: ModuleDependencyCapabilitiesHandler? = null,
    override val endorseStrictVersions: Boolean? = null,
    override val doNotEndorseStrictVersions: Boolean? = null,
    override val because: String? = null,
    /**
     * Sets the dependency as "changing" or "not changing".
     * If set to true, the dependency is marked as "changing." Gradle will periodically check the remote repository for updates, even if the local cache entry has not yet expired.
     * Defaults to false.
     *
     * @param changing if true, the dependency is considered changing and Gradle should
     * check for a change in the remote repository, even if a local entry exists.
     * @return this
     */
    val changing: Boolean? = null,
) : ExternalDependency<ExternalModuleDependency> {

    context(Project)
    override fun applyTo(receiver: ExternalModuleDependency) {
        super.applyTo(receiver)

        receiver::setChanging trySet changing
    }
}
