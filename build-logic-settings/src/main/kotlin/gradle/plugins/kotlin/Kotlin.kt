package gradle.plugins.kotlin

import gradle.api.artifacts.DependencyArtifact
import gradle.api.artifacts.ExcludeRule
import gradle.api.artifacts.ExternalModuleDependency
import gradle.api.artifacts.ModuleDependencyCapabilitiesHandler
import gradle.api.artifacts.MutableVersionConstraint
import kotlinx.serialization.Serializable

@Serializable
internal data class Kotlin(
    override val changing: Boolean? = null,
    override val version: MutableVersionConstraint? = null,
    override val excludeRules: Set<ExcludeRule>? = null,
    override val artifacts: Set<DependencyArtifact>? = null,
    override val transitive: Boolean? = null,
    override val targetConfiguration: String? = null,
    override val capabilities: ModuleDependencyCapabilitiesHandler? = null,
    override val endorseStrictVersions: Boolean? = null,
    override val doNotEndorseStrictVersions: Boolean? = null,
    override val because: String? = null,
    val simpleModuleName: String,
) : ExternalModuleDependency
