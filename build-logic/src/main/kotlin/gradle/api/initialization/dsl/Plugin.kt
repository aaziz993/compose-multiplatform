package gradle.api.initialization.dsl

import gradle.api.artifacts.VersionConstraint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint
import org.gradle.api.internal.artifacts.dependencies.DefaultPluginDependency
import org.gradle.plugin.use.PluginDependency

@Serializable
public data class Plugin(
    @SerialName("id")
    private val _pluginId: String,
    @SerialName("version")
    private val _version: VersionConstraint,
) : PluginDependency by DefaultPluginDependency(
    _pluginId,
    DefaultMutableVersionConstraint(_version)
)
