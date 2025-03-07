package plugin.project.kotlin.ksp.model

import gradle.id
import gradle.libs
import gradle.model.kotlin.ksp.KspExtension
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.project.EnabledSettings

@Serializable
internal data class KspSettings(
    override val useKsp2: Boolean? = null,
    override val commandLineArgumentProviders: List<String>? = null,
    override val excludedProcessors: List<String>? = null,
    override val excludedSources: List<String>? = null,
    override val arguments: Map<String, String>? = null,
    override val allWarningsAsErrors: Boolean? = null,
    override val enabled: Boolean = true,
) : KspExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("ksp").id) {
            super.applyTo()
        }
}
