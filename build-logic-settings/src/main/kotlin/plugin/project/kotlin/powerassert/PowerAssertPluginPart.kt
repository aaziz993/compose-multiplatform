package plugin.project.kotlin.powerassert

import gradle.amperModuleExtraProperties
import gradle.libs
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class PowerAssertPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    override val needToApply: Boolean by lazy {
        project.amperModuleExtraProperties.settings.kotlin.powerAssert.enabled
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.libs.plugins.power.assert.get().pluginId)

        applySettings()
    }

    private fun applySettings() = with(project) {
        configurePowerAssertGradleExtension()
    }
}
