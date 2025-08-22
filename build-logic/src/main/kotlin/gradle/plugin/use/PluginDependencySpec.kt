package gradle.plugin.use

import klib.data.type.primitives.string.addPrefixIfNotEmpty

public class PluginDependencySpec(
    public val pluginId: String
) : org.gradle.plugin.use.PluginDependencySpec {

    public var version: String? = null
        private set

    public var apply: Boolean = true
        private set

    override fun version(version: String?): PluginDependencySpec = apply {
        this.version = version
    }

    override fun apply(apply: Boolean): PluginDependencySpec = apply {
        this.apply = apply
    }

    override fun toString(): String =
        "$pluginId:$pluginId.gradle.plugin${version.toString().addPrefixIfNotEmpty(":")}"
}
