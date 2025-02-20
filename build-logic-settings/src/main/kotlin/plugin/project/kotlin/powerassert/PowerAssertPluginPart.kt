package plugin.project.kotlin.powerassert

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class PowerAssertPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.powerAssert.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.power.assert.get().pluginId)

            configurePowerAssertGradleExtension()
        }
    }
}
