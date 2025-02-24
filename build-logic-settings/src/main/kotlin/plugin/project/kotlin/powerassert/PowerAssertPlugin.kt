package plugin.project.kotlin.powerassert

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class PowerAssertPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.powerAssert
                .takeIf { it.enabled && projectProperties.kotlin.hasTargets }?.let { powerAssert ->
                    plugins.apply(settings.libs.plugins.plugin("power.assert").id)

                    powerAssert.applyTo()
                }
        }
    }
}
