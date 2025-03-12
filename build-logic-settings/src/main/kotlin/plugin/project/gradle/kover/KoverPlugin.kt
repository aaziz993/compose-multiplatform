package plugin.project.gradle.kover

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KoverPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.kover
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { kover ->

                    plugins.apply(settings.libs.plugins.plugin("kover").id)

                    kover.applyTo()
                }
        }
    }
}
