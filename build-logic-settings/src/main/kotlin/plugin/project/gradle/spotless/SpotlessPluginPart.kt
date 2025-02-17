package plugin.project.gradle.spotless

import gradle.amperModuleExtraProperties
import gradle.libs
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class SpotlessPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val spotless by lazy {
        project.amperModuleExtraProperties.settings.gradle.spotless
    }

    override val needToApply: Boolean by lazy {
        spotless.enabled
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.libs.plugins.spotless.get().pluginId)

        applySettings()
    }

    fun applySettings() = with(project) {
        configureSpotlessExtension()
    }
}
