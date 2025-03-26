package plugins.kotlin.powerassert

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugins.kotlin.powerassert.model.PowerAssertSettings

internal class PowerAssertPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.powerAssert
                .takeIf(PowerAssertSettings::enabled)?.let { powerAssert ->
                    plugins.apply(project.settings.libs.plugins.plugin("powerAssert").id)

                    powerAssert.applyTo()
                }
        }
    }
}
