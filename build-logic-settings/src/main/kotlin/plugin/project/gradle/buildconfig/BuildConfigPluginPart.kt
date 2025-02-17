package plugin.project.gradle.buildconfig

import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import plugin.project.gradle.spotless.configureSpotlessExtension

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
