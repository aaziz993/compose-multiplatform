package plugin.project.kotlin.ksp

import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.jetbrains.amper.frontend.schema.KspSettings
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class KspPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    override val needToApply: Boolean by lazy {
        project.amperModuleExtraProperties.settings.kotlin.ksp2.enabled
    }

    override fun applyBeforeEvaluate() {
        project.plugins.apply(project.settings.libs.plugins.plugin("atomicfu").id)

        applySettings()
    }

    private fun applySettings() = with(project) {
        configureKspExtension()
    }
}
