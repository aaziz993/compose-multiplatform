package plugin.project.gradle.dokka.model

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.Configuration
import org.jetbrains.dokka.DokkaConfiguration

@Serializable
internal data class DokkaModuleTask(
    override val moduleName: String? = null,
    override val moduleVersion: String? = null,
    override val outputDirectory: String? = null,
    override val pluginsConfiguration: List<DokkaConfiguration.PluginConfiguration>? = null,
    override val pluginsMapConfiguration: Map<String, String>? = null,
    override val suppressObviousFunctions: Boolean? = null,
    override val suppressInheritedMembers: Boolean? = null,
    override val offlineMode: Boolean? = null,
    override val failOnWarning: Boolean? = null,
    override val cacheRoot: String? = null,
) : DokkaTask
