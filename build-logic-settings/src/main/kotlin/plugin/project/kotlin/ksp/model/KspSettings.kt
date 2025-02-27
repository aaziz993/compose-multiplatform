package plugin.project.kotlin.ksp.model

import gradle.id
import gradle.ksp
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.tryAssign
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.model.dependency.ProjectDependency
import plugin.project.model.EnabledSettings

@Serializable
internal data class KspSettings(
    override val useKsp2: Boolean? = null,
    override val commandLineArgumentProviders: List<String>? = null,
    override val excludedProcessors: List<String>? = null,
    override val excludedSources: List<String>? = null,
    override val arguments: Map<String, String>? = null,
    override val allWarningsAsErrors: Boolean? = null,
    val processors: List<ProjectDependency>? = null,
    override val enabled: Boolean = true,
) : KspExtension, EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("ksp").id) {
            ksp.useKsp2 tryAssign useKsp2

            commandLineArgumentProviders?.let { commandLineArgumentProviders ->
                ksp.arg { commandLineArgumentProviders }
            }

            excludedProcessors?.forEach(ksp::excludeProcessor)
            excludedSources?.let(ksp.excludedSources::setFrom)
            arguments?.forEach { (key, value) -> ksp.arg(key, value) }
            ksp::allWarningsAsErrors trySet allWarningsAsErrors
        }
}
