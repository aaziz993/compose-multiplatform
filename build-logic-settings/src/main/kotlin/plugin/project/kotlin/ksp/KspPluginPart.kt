package plugin.project.kotlin.ksp

import gradle.moduleProperties
import gradle.libs
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class KspPluginPart(override val project: Project) : BindingPluginPart {

    private val ksp by lazy {
        project.moduleProperties.settings.kotlin.ksp2
    }

    override val needToApply: Boolean by lazy {
        ksp.enabled
    }

    override fun applyAfterEvaluate() =with(project) {
        plugins.apply(project.libs.plugins.ksp.get().pluginId)

        applySettings()
    }

    private fun applySettings() = with(project) {
        configureKspExtension()

        val kspCommonMainMetadata by configurations
        dependencies {
            ksp.processors?.forEach { processor ->
                kspCommonMainMetadata(processor.toDependencyNotation())
            }
        }
    }
}
