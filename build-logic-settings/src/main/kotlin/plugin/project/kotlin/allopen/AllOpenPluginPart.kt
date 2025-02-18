package plugin.project.kotlin.allopen

import gradle.amperModuleExtraProperties
import gradle.libs
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import plugin.project.kotlin.ksp.configureKspExtension

internal class AllOpenPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    override val needToApply: Boolean by lazy {
        project.amperModuleExtraProperties.settings.kotlin.allOpen.enabled
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.libs.plugins.allopen.get().pluginId)

        applySettings()
    }

    private fun applySettings() = with(project) {
        configureAllOpenExtension()
    }
}
