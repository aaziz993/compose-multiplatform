package plugin.project.kotlin.powerassert

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class PowerAssertPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.powerAssert.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.power.assert.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configurePowerAssertGradleExtension()
        }
    }
}
