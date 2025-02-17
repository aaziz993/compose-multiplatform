package plugin.project.gradle.buildconfig

import gradle.amperModuleExtraProperties
import gradle.libs
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class BuildConfigPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    override val needToApply: Boolean by lazy {
        project.amperModuleExtraProperties.settings.gradle.buildConfig.enabled
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.libs.plugins.build.config.get().pluginId)

        applySettings()
    }

    fun applySettings() = with(project) {
        configureBuildConfigExtension()
    }
}
