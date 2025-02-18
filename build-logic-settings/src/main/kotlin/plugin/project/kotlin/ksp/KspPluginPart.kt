package plugin.project.kotlin.ksp

import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class KspPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val ksp by lazy {
        project.amperModuleExtraProperties.settings.kotlin.ksp2
    }

    override val needToApply: Boolean by lazy {
        ksp.enabled
    }

    override fun applyBeforeEvaluate() {
        project.plugins.apply(project.settings.libs.plugins.plugin("ksp").id)

        applySettings()
    }

    private fun applySettings() = with(project) {
        configureKspExtension()

        val kspCommonMainMetadata by configurations
        dependencies {
            ksp.processors?.forEach { processor ->
                kspCommonMainMetadata(processor.toDependencyNotation(layout.projectDirectory))
            }
        }
    }
}
