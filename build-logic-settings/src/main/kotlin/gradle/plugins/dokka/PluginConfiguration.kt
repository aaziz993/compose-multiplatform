package gradle.plugins.dokka

import kotlinx.serialization.Serializable
import org.jetbrains.dokka.DokkaConfiguration

@Serializable
internal data class PluginConfiguration(
    override val fqPluginName: String,
    override val serializationFormat: DokkaConfiguration.SerializationFormat,
    override val values: String
) : DokkaConfiguration.PluginConfiguration
