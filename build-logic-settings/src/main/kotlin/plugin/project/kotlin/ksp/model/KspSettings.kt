package plugin.project.kotlin.ksp.model

import gradle.id
import gradle.ksp
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.tryAssign
import gradle.trySet
import kotlin.io.resolve
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import plugin.model.dependency.Dependency
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer
import plugin.project.model.EnabledSettings

@Serializable
internal data class KspSettings(
    override val useKsp2: Boolean? = null,
    override val commandLineArgumentProviders: List<String>? = null,
    override val excludedProcessors: List<String>? = null,
    override val excludedSources: List<String>? = null,
    override val arguments: Map<String, String>? = null,
    override val allWarningsAsErrors: Boolean? = null,
    val processors: List<@Serializable(with = ProjectDependencyTransformingSerializer::class) ProjectDependency>? = null,
    override val enabled: Boolean = true,
) : KspExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("ksp").id) {
            super.applyTo()

            val kspCommonMainMetadata by configurations
            dependencies {
                processors?.filterIsInstance<Dependency>()?.forEach { processor ->
                    kspCommonMainMetadata(processor.resolve())
                }
            }
        }
}
