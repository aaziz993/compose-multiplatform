package gradle.plugins.kotlin.powerassert

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.powerassert.model.PowerAssertSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class PowerAssertPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.powerAssert?.takeIf{ pluginManager.hasPlugin("powerAssert") }?.let { powerAssert ->
                    plugins.apply(project.settings.libs.plugin("powerAssert").id)

                    powerAssert.applyTo()
                }
        }
    }
}
