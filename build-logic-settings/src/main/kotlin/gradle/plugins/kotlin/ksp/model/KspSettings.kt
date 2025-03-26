package gradle.plugins.kotlin.ksp.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.ksp.KspExtension
import gradle.plugins.project.EnabledSettings
import gradle.process.CommandLineArgumentProvider
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KspSettings(
    override val useKsp2: Boolean? = null,
    override val commandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
    override val excludedProcessors: Set<String>? = null,
    override val excludedSources: Set<String>? = null,
    override val setExcludedSources: Set<String>? = null,
    override val arguments: Map<String, String>? = null,
    override val allWarningsAsErrors: Boolean? = null,
    override val enabled: Boolean = true,
) : KspExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("ksp").id) {
            super.applyTo()
        }
}
