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

    private val spotless by lazy {
        project.amperModuleExtraProperties.settings.gradle.spotless
    }

    override val needToApply: Boolean by lazy {
        spotless.enabled
    }

    override fun applyBeforeEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.settings.libs.plugins.plugin("build-config").id)

        applySettings()
    }

    fun applySettings() = with(project) {
        configureBuildConfigExtension()
    }
}
