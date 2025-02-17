package plugin.project.gradle.kover

import gradle.amperModuleExtraProperties
import gradle.libs
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class KoverPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    override val needToApply: Boolean by lazy {
        project.amperModuleExtraProperties.settings.gradle.kover.enabled
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.libs.plugins.kover.get().pluginId)

        applySettings()
    }

    private fun applySettings() = with(project) {
        configureKoverExtension()
    }
}
