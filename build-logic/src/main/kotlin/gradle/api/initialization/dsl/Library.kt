package gradle.api.initialization.dsl

import gradle.api.artifacts.VersionConstraint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableMinimalDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint
import org.gradle.api.internal.artifacts.dependencies.MinimalExternalModuleDependencyInternal

@Suppress("PropertyName")
@Serializable
public data class Library(
    @SerialName("group")
    val _group: String,
    @SerialName("name")
    val _name: String,
    @SerialName("version")
    val _version: VersionConstraint = VersionConstraint(require = ""),
) : MinimalExternalModuleDependencyInternal by DefaultMutableMinimalDependency(
    DefaultModuleIdentifier.newId(_group, _name),
    DefaultMutableVersionConstraint(_version),
    null
)
