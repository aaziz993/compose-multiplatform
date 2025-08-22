package gradle.api.initialization.dsl

import gradle.api.artifacts.VersionConstraint
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.plugin.use.PluginDependency

@Serializable
public data class Plugin(
    @SerialName("id")
    private val _pluginId: String,
    @SerialName("version")
    private val _version: VersionConstraint,
) : PluginDependency {

    override fun getPluginId(): String = _pluginId

    override fun getVersion(): org.gradle.api.artifacts.VersionConstraint = _version
}
