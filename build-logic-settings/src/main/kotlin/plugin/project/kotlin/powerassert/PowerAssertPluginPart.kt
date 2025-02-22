package plugin.project.kotlin.powerassert

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class PowerAssertPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (settings.projectProperties.plugins.powerAssert.enabled || settings.projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.power.assert.get().pluginId)

            configurePowerAssertGradleExtension()
        }
    }
}
