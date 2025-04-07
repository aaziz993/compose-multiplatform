package gradle.plugins.kotlin

import gradle.api.artifacts.DependencyArtifact
import gradle.api.artifacts.ExcludeRule
import gradle.api.artifacts.ModuleDependencyCapabilitiesHandler
import gradle.api.artifacts.ProjectDependency
import kotlinx.serialization.Serializable

@Serializable
internal data class Project(
    override val path: String,
    override val excludeRules: Set<ExcludeRule>? = null,
    override val artifacts: Set<DependencyArtifact>? = null,
    override val transitive: Boolean? = null,
    override val targetConfiguration: String? = null,
    override val capabilities: ModuleDependencyCapabilitiesHandler? = null,
    override val endorseStrictVersions: Boolean? = null,
    override val doNotEndorseStrictVersions: Boolean? = null,
    override val because: String? = null,
    val configuration: String? = null,
) : ProjectDependency<org.gradle.api.artifacts.ProjectDependency>
