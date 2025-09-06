package gradle.api.initialization.dsl

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency

@Serializable
public data class Bundle(
    val libraries: MutableList<MinimalExternalModuleDependency>,
) : ExternalModuleDependencyBundle, MutableList<MinimalExternalModuleDependency> by libraries
